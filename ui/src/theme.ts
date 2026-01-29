import { createTheme } from '@mui/material/styles';

// Centralized color palette
export const colors = {
  deepTwilight: '#03045eff',
  brightTealBlue: '#0077b6ff',
  turquoiseSurf: '#00b4d8ff',
  frostedBlue: '#90e0efff',
  lightCyan: '#caf0f8ff',
};

// Create Material-UI theme with custom color palette
const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: colors.brightTealBlue,
      dark: colors.deepTwilight,
      light: colors.turquoiseSurf,
    },
    secondary: {
      main: colors.frostedBlue,
      light: colors.lightCyan,
    },
    background: {
      default: '#ffffff',
      paper: '#ffffff',
    },
  },
  components: {
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundColor: colors.deepTwilight,
        },
      },
    },
    MuiButton: {
      styleOverrides: {
        contained: {
          '&:hover': {
            backgroundColor: colors.turquoiseSurf,
          },
        },
      },
    },
  },
});

export default theme;

