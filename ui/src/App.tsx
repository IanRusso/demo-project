import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import {
  ThemeProvider,
  CssBaseline,
  Container,
  AppBar,
  Toolbar,
  Typography,
  Box,
  Button,
  Grid,
  Paper,
} from '@mui/material';
import {
  Dashboard as DashboardIcon,
  Login as LoginIcon,
  AccountCircle as ProfileIcon,
} from '@mui/icons-material';
import theme, { colors } from './theme';
import CreateUser from './pages/CreateUser';
import Login from './pages/Login';
import Profile from './pages/Profile';

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
      <CssBaseline />
      <Router>
        <Box sx={{ flexGrow: 1, minHeight: '100vh', backgroundColor: colors.lightCyan }}>
          <AppBar position="static">
            <Toolbar>
              <DashboardIcon sx={{ mr: 2 }} />
              <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                Gainfully
              </Typography>
              <Button
                color="inherit"
                component={Link}
                to="/"
                sx={{ mr: 2 }}
              >
                Dashboard
              </Button>
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
            <Route path="/" element={<Dashboard />} />
            <Route path="/login" element={<Login onLogin={handleLogin} />} />
            <Route path="/create-user" element={<CreateUser onRegister={handleRegister} />} />
            <Route path="/profile" element={<Profile user={user} onLogout={handleLogout} onUpdateUser={handleUpdateUser} />} />
          </Routes>
        </Box>
      </Router>
    </ThemeProvider>
  );
};

// Dashboard component extracted from main App
const Dashboard: React.FC = () => {
  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Paper sx={{ p: 3, textAlign: 'center' }}>
            <Typography variant="h3" gutterBottom>
              Welcome to Gainfully
            </Typography>
            <Typography variant="body1" color="text.secondary" paragraph>
              Connect talent with opportunity
            </Typography>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
};

export default App;

