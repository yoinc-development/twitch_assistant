import { defineConfig } from 'vite';
import { resolve } from 'path';
import { fileURLToPath } from 'url';
import { dirname } from 'path';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

export default defineConfig({
  base: './',
  root: __dirname,
  build: {
    outDir: 'dist',
    emptyOutDir: true,
    assetsDir: '.',
    rollupOptions: {
      input: {
        main: resolve(__dirname, 'index.html'),
      },
      output: {
        entryFileNames: '[name].js',
        chunkFileNames: '[name].js',
        assetFileNames: '[name].[ext]',
      },
    },
  },
  server: {
    port: 3000,
    strictPort: true,
  },
  plugins: [
    {
      name: 'copy-preload',
      closeBundle() {
        const fs = require('fs');
        const path = require('path');
        
        if (process.env.NODE_ENV === 'production') {
          const preloadPath = path.join(__dirname, 'preload.mjs');
          const destPath = path.join(__dirname, 'dist', 'preload.mjs');
          
          if (fs.existsSync(preloadPath)) {
            fs.copyFileSync(preloadPath, destPath);
          }
        }
      },
    },
  ],
});
