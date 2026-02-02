import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import {
  ThemeProvider,
  CssBaseline,
  AppBar,
  Toolbar,
  Typography,
  Box,
  Button,
} from '@mui/material';
import {
  Dashboard as DashboardIcon,
  Login as LoginIcon,
  AccountCircle as ProfileIcon,
} from '@mui/icons-material';
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import theme, { colors } from './theme';
import CreateUser from './pages/CreateUser';
import Login from './pages/Login';
import Profile from './pages/Profile';
import Home from './pages/Home';

const App: React.FC = () => {
  const [user, setUser] = useState<any | null>(null);

  // Load user from localStorage on mount
  useEffect(() => {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      try {
        setUser(JSON.parse(storedUser));
      } catch (e) {
        console.error('Failed to parse stored user:', e);
        localStorage.removeItem('user');
      }
    }
  }, []);

  const handleLogin = (userData: any) => {
    setUser(userData);
  };

  const handleLogout = () => {
    setUser(null);
    localStorage.removeItem('user');
  };

  const handleRegister = (userData: any) => {
    setUser(userData);
  };

  const handleUpdateUser = (userData: any) => {
    setUser(userData);
  };

  return (
    <ThemeProvider theme={theme}>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <CssBaseline />
        <Router>
          <Box sx={{ flexGrow: 1, minHeight: '100vh', backgroundColor: colors.lightCyan }}>
            <AppBar position="static">
              <Toolbar>
                <Box
                  component={Link}
                  to="/"
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    flexGrow: 1,
                    textDecoration: 'none',
                    color: 'inherit',
                    cursor: 'pointer',
                    '&:hover': {
                      opacity: 0.8,
                    },
                  }}
                >
                  <DashboardIcon sx={{ mr: 2 }} />
                  <Typography variant="h6">
                    Gainfully
                  </Typography>
                </Box>
                {user ? (
                  <Button
                    color="inherit"
                    component={Link}
                    to="/profile"
                    startIcon={<ProfileIcon />}
                  >
                    Profile
                  </Button>
                ) : (
                  <Button
                    color="inherit"
                    component={Link}
                    to="/login"
                    startIcon={<LoginIcon />}
                  >
                    Login
                  </Button>
                )}
              </Toolbar>
            </AppBar>

            <Routes>
              <Route path="/" element={<Home user={user} />} />
              <Route path="/login" element={<Login onLogin={handleLogin} />} />
              <Route path="/create-user" element={<CreateUser onRegister={handleRegister} />} />
              <Route path="/profile" element={<Profile user={user} onLogout={handleLogout} onUpdateUser={handleUpdateUser} />} />
            </Routes>
          </Box>
        </Router>
      </LocalizationProvider>
    </ThemeProvider>
  );
};

export default App;

