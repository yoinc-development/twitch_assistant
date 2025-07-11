import { contextBridge, ipcRenderer } from 'electron';

try {
  contextBridge.exposeInMainWorld('electron', {
    send: (channel, data) => {
      const validChannels = [
        'app:minimize',
        'app:maximize',
        'app:close',
        'app:get-version',
        'app:open-external'
      ];
      
      if (validChannels.includes(channel)) {
        ipcRenderer.send(channel, data);
      }
    },
    
    receive: (channel, func) => {
      const validChannels = [
        'app:version',
        'app:notification'
      ];
      
      if (validChannels.includes(channel)) {
        ipcRenderer.on(channel, (event, ...args) => func(...args));
      }
    },
    
    invoke: async (channel, data) => {
      const validChannels = [
        'app:get-version',
        'dialog:show-open-dialog',
        'dialog:show-save-dialog',
        'dialog:show-message-box'
      ];
      
      if (validChannels.includes(channel)) {
        return await ipcRenderer.invoke(channel, data);
      }
      
      throw new Error(`Invalid channel: ${channel}`);
    },
    
    env: {
      NODE_ENV: import.meta.env.NODE_ENV,
      PLATFORM: process.platform
    }
  });
} catch (error) {
  console.error('Failed to expose electron API:', error);
}

window.addEventListener('unhandledrejection', (event) => {
  console.error('Unhandled promise rejection:', event.reason);
  ipcRenderer.send('app:error', event.reason);
});