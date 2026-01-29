import React, { useState } from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import {
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Box,
  Alert,
  CircularProgress,
  Link,
  Divider,
} from '@mui/material';
import { Login as LoginIcon, PersonAdd as PersonAddIcon } from '@mui/icons-material';
import { colors } from '../theme';

interface LoginFormData {
  email: string;
  password: string;
}

interface LoginProps {
  onLogin: (user: any) => void;
}

const Login: React.FC<LoginProps> = ({ onLogin }) => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<LoginFormData>({
    email: '',
    password: '',
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          email: formData.email,
          password: formData.password,
        }),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Login failed');
      }

      const result = await response.json();
      console.log('Login successful:', result);
      setSuccess(true);

      // Store user data in localStorage
      if (result.data) {
        localStorage.setItem('user', JSON.stringify(result.data));
        onLogin(result.data);
      }

      // Redirect to home page after 1.5 seconds
      setTimeout(() => {
        navigate('/');
      }, 1500);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'An error occurred during login');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="sm" sx={{ mt: 8, mb: 4 }}>
      <Paper
        elevation={3}
        sx={{
          p: 4,
          borderTop: `4px solid ${colors.brightTealBlue}`,
        }}
      >
        <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', mb: 3 }}>
          <LoginIcon
            sx={{ fontSize: 40, mr: 2, color: colors.deepTwilight }}
          />
          <Typography variant="h4" component="h1" sx={{ color: colors.deepTwilight }}>
            Login
          </Typography>
        </Box>

        {error && (
          <Alert severity="error" sx={{ mb: 3 }} onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {success && (
          <Alert severity="success" sx={{ mb: 3 }} onClose={() => setSuccess(false)}>
            Login successful! Welcome back.
          </Alert>
        )}

        <form onSubmit={handleSubmit}>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3 }}>
            <TextField
              required
              fullWidth
              label="Email"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              variant="outlined"
              autoComplete="email"
              autoFocus
            />

            <TextField
              required
              fullWidth
              label="Password"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              variant="outlined"
              autoComplete="current-password"
            />

            <Button
              type="submit"
              fullWidth
              variant="contained"
              disabled={loading}
              sx={{
                py: 1.5,
                backgroundColor: colors.brightTealBlue,
                '&:hover': {
                  backgroundColor: colors.deepTwilight,
                },
              }}
            >
              {loading ? <CircularProgress size={24} /> : 'Login'}
            </Button>
          </Box>
        </form>

        <Divider sx={{ my: 3 }} />

        <Box sx={{ textAlign: 'center' }}>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
            Don't have an account?
          </Typography>
          <Button
            component={RouterLink}
            to="/create-user"
            variant="outlined"
            startIcon={<PersonAddIcon />}
            fullWidth
            sx={{
              borderColor: colors.brightTealBlue,
              color: colors.brightTealBlue,
              '&:hover': {
                borderColor: colors.deepTwilight,
                backgroundColor: colors.lightCyan,
              },
            }}
          >
            Create New Account
          </Button>
        </Box>

        <Box sx={{ mt: 3, textAlign: 'center' }}>
          <Link
            component={RouterLink}
            to="/"
            sx={{
              color: colors.brightTealBlue,
              textDecoration: 'none',
              '&:hover': {
                textDecoration: 'underline',
              },
            }}
          >
            Back to Dashboard
          </Link>
        </Box>
      </Paper>
    </Container>
  );
};

export default Login;

