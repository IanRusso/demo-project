import React, { useState, useEffect } from 'react';
import {
  ThemeProvider,
  createTheme,
  CssBaseline,
  Container,
  AppBar,
  Toolbar,
  Typography,
  Box,
  Card,
  CardContent,
  Button,
  Grid,
  Paper,
  Chip,
  CircularProgress,
  Alert,
} from '@mui/material';
import {
  Dashboard as DashboardIcon,
  FiberManualRecord as StatusIcon,
} from '@mui/icons-material';

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

interface HealthCheck {
  healthy: boolean;
  message?: string;
}

interface HealthData {
  'demo-server': HealthCheck;
  deadlocks: HealthCheck;
}

const App: React.FC = () => {
  const [count, setCount] = useState<number>(0);
  const [healthData, setHealthData] = useState<HealthData | null>(null);
  const [healthLoading, setHealthLoading] = useState<boolean>(true);
  const [healthError, setHealthError] = useState<string | null>(null);

  const fetchHealth = async () => {
    try {
      const response = await fetch('http://localhost:8081/healthcheck');
      if (!response.ok) {
        throw new Error('Failed to fetch health status');
      }
      const data = await response.json();
      setHealthData(data);
      setHealthError(null);
    } catch (error) {
      setHealthError(error instanceof Error ? error.message : 'Unknown error');
      setHealthData(null);
    } finally {
      setHealthLoading(false);
    }
  };

  useEffect(() => {
    // Fetch health immediately
    fetchHealth();

    // Poll health every 5 seconds
    const interval = setInterval(fetchHealth, 5000);

    return () => clearInterval(interval);
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Box sx={{ flexGrow: 1 }}>
        <AppBar position="static">
          <Toolbar>
            <DashboardIcon sx={{ mr: 2 }} />
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
              Demo Project
            </Typography>
          </Toolbar>
        </AppBar>

        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
          <Grid container spacing={3}>
            <Grid item xs={12}>
              <Paper sx={{ p: 3, textAlign: 'center' }}>
                <Typography variant="h3" gutterBottom>
                  Welcome to Demo Project
                </Typography>
                <Typography variant="body1" color="text.secondary" paragraph>
                  This is a React + TypeScript + Material UI demo application
                </Typography>
              </Paper>
            </Grid>

            {/* Backend Health Status Card */}
            <Grid item xs={12}>
              <Card>
                <CardContent>
                  <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', mb: 2 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <StatusIcon color="primary" sx={{ mr: 1 }} />
                      <Typography variant="h6">Backend Server Health</Typography>
                    </Box>
                    {healthLoading && <CircularProgress size={24} />}
                  </Box>

                  {healthError && (
                    <Alert severity="error" sx={{ mb: 2 }}>
                      {healthError} - Make sure the backend server is running on port 8081 (admin port)
                    </Alert>
                  )}

                  {healthData && (
                    <Grid container spacing={2}>
                      <Grid item xs={12} md={6}>
                        <Box sx={{
                          textAlign: 'center',
                          p: 3,
                          bgcolor: healthData['demo-server']?.healthy ? 'success.light' : 'error.light',
                          borderRadius: 2
                        }}>
                          <Typography variant="subtitle2" color={healthData['demo-server']?.healthy ? 'success.dark' : 'error.dark'} gutterBottom>
                            Demo Server
                          </Typography>
                          <Chip
                            icon={<StatusIcon />}
                            label={healthData['demo-server']?.healthy ? 'HEALTHY' : 'UNHEALTHY'}
                            color={healthData['demo-server']?.healthy ? 'success' : 'error'}
                            sx={{ mt: 1 }}
                          />
                          {healthData['demo-server']?.message && (
                            <Typography variant="caption" display="block" sx={{ mt: 1 }} color={healthData['demo-server']?.healthy ? 'success.dark' : 'error.dark'}>
                              {healthData['demo-server'].message}
                            </Typography>
                          )}
                        </Box>
                      </Grid>

                      <Grid item xs={12} md={6}>
                        <Box sx={{
                          textAlign: 'center',
                          p: 3,
                          bgcolor: healthData.deadlocks?.healthy ? 'success.light' : 'error.light',
                          borderRadius: 2
                        }}>
                          <Typography variant="subtitle2" color={healthData.deadlocks?.healthy ? 'success.dark' : 'error.dark'} gutterBottom>
                            Deadlocks Check
                          </Typography>
                          <Chip
                            icon={<StatusIcon />}
                            label={healthData.deadlocks?.healthy ? 'NO DEADLOCKS' : 'DEADLOCKS DETECTED'}
                            color={healthData.deadlocks?.healthy ? 'success' : 'error'}
                            sx={{ mt: 1 }}
                          />
                          {healthData.deadlocks?.message && (
                            <Typography variant="caption" display="block" sx={{ mt: 1 }} color={healthData.deadlocks?.healthy ? 'success.dark' : 'error.dark'}>
                              {healthData.deadlocks.message}
                            </Typography>
                          )}
                        </Box>
                      </Grid>

                      <Grid item xs={12}>
                        <Typography variant="caption" color="text.secondary">
                          Last updated: {new Date().toLocaleTimeString()}
                        </Typography>
                      </Grid>
                    </Grid>
                  )}

                  {!healthData && !healthError && !healthLoading && (
                    <Typography variant="body2" color="text.secondary">
                      No health data available
                    </Typography>
                  )}
                </CardContent>
              </Card>
            </Grid>

            <Grid item xs={12}>
              <Paper sx={{ p: 3, textAlign: 'center' }}>
                <Typography variant="h5" gutterBottom>
                  Interactive Counter Demo
                </Typography>
                <Typography variant="h2" color="primary" sx={{ my: 3 }}>
                  {count}
                </Typography>
                <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center' }}>
                  <Button
                    variant="contained"
                    color="primary"
                    onClick={() => setCount(count + 1)}
                  >
                    Increment
                  </Button>
                  <Button
                    variant="outlined"
                    color="secondary"
                    onClick={() => setCount(0)}
                  >
                    Reset
                  </Button>
                  <Button
                    variant="contained"
                    color="secondary"
                    onClick={() => setCount(count - 1)}
                  >
                    Decrement
                  </Button>
                </Box>
              </Paper>
            </Grid>
          </Grid>
        </Container>
      </Box>
    </ThemeProvider>
  );
};

export default App;

