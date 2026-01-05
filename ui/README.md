# Demo Project UI

A React + TypeScript + Material UI demo application.

## Prerequisites

- Node.js (v16 or higher)
- npm (v7 or higher)

## Getting Started

### Install Dependencies

```bash
npm install
```

### Development

Start the development server:

```bash
npm start
```

This will start the webpack dev server at `http://localhost:3000` and automatically open it in your browser.

For development without auto-opening the browser:

```bash
npm run dev
```

### Production Build

Build the application for production:

```bash
npm run build
```

The production-ready files will be generated in the `dist/` directory.

## Project Structure

```
ui/
├── public/
│   └── index.html          # HTML template
├── src/
│   ├── App.tsx             # Main application component
│   └── index.tsx           # Application entry point
├── package.json            # Dependencies and scripts
├── tsconfig.json           # TypeScript configuration
└── webpack.config.js       # Webpack configuration
```

## Features

- ✅ React 18
- ✅ TypeScript
- ✅ Material UI (MUI) v5
- ✅ Webpack 5
- ✅ Hot Module Replacement (HMR)
- ✅ Production optimizations

## Technologies

- **React**: UI library
- **TypeScript**: Type-safe JavaScript
- **Material UI**: React component library
- **Webpack**: Module bundler
- **Emotion**: CSS-in-JS styling (used by MUI)

