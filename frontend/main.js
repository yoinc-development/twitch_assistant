import {app, BrowserWindow, ipcMain} from 'electron';
import path from 'path';
import {fileURLToPath} from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

let mainWindow;

function createWindow() {
    const isDev = process.env.NODE_ENV === 'development';
    const preloadPath = isDev
        ? path.join(__dirname, 'preload.mjs')
        : path.join(process.resourcesPath, 'app.asar/dist/preload.mjs');

    // Create the browser window with modern settings
    mainWindow = new BrowserWindow({
        width: 900,
        height: 600,
        minWidth: 900,
        minHeight: 600,
        backgroundColor: '#1e1e2e',
        titleBarStyle: 'default',
        frame: true,
        resizable: false,
        webPreferences: {
            nodeIntegration: false,
            contextIsolation: true,
            enableRemoteModule: false,
            preload: preloadPath,
            webSecurity: true,
            devTools: isDev
        },
        show: false
    });

    mainWindow.webContents.openDevTools()

    // Load application
    const loadApp = () => {
        if (process.env.NODE_ENV === 'development') {
            mainWindow.loadURL('http://localhost:3000')
                .catch(err => {
                    // Retry after 1 second if the first attempt fails
                    setTimeout(() => {
                        mainWindow.loadURL('http://localhost:3000')
                            .then(() => console.log('Successfully loaded after retry'))
                            .catch(e => console.error('Failed to load after retry:', e));
                    }, 1000);
                });
            mainWindow.webContents.openDevTools({mode: 'detach'});
        } else {
            mainWindow.loadFile(path.join(__dirname, 'index.html'));
        }
    };

    // Delay for vite
    if (process.env.NODE_ENV === 'development') {
        setTimeout(loadApp, 1000);
    } else {
        loadApp();
    }

    mainWindow.once('ready-to-show', () => {
        mainWindow.show();
    });

    mainWindow.on('closed', () => {
        mainWindow = null;
    });
}

app.whenReady().then(() => {
    createWindow();
    // Mac-OS specific event
    app.on('activate', () => {
        if (BrowserWindow.getAllWindows().length === 0) {
            createWindow();
        }
    });
});

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit();
    }
});

// Handle any uncaught exceptions
process.on('uncaughtException', (error) => {
    console.error('Uncaught Exception:', error);
});

// IPC Communication
ipcMain.handle('app:get-version', () => {
    return app.getVersion();
});

// Disable hardware acceleration
app.disableHardwareAcceleration();

app.enableSandbox();
