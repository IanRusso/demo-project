import React, { useState } from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import {
  Container,
  Paper,
  Typography,
  TextField,
  Button,
  Grid,
  Box,
  Alert,
  CircularProgress,
  MenuItem,
  FormControlLabel,
  Checkbox,
  Link,
  Divider,
} from '@mui/material';
import { PersonAdd as PersonAddIcon, Login as LoginIcon } from '@mui/icons-material';
import { colors } from '../theme';

interface UserFormData {
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
  phoneNumber: string;
  location: string;
  educationLevel: string;
  summary: string;
  profilePictureUrl: string;
  employmentStatus: string;
  salaryExpectationsMin: string;
  salaryExpectationsMax: string;
  activelySeeking: boolean;
}

const educationLevels = [
  'High School',
  'Associate Degree',
  "Bachelor's Degree",
  "Master's Degree",
  'Doctorate',
  'Other',
];

const employmentStatuses = [
  'Employed',
  'Unemployed',
  'Self-Employed',
  'Student',
  'Retired',
];

interface CreateUserProps {
  onRegister: (user: any) => void;
}

const CreateUser: React.FC<CreateUserProps> = ({ onRegister }) => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState<UserFormData>({
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    phoneNumber: '',
    location: '',
    educationLevel: '',
    summary: '',
    profilePictureUrl: '',
    employmentStatus: '',
    salaryExpectationsMin: '',
    salaryExpectationsMax: '',
    activelySeeking: false,
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccess(false);

    // Validate passwords match
    if (formData.password !== formData.confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    // Validate password length
    if (formData.password.length < 8) {
      setError('Password must be at least 8 characters long');
      setLoading(false);
      return;
    }

    try {
      // Use the auth/register endpoint which handles password
      const registerPayload = {
        name: formData.name,
        email: formData.email,
        password: formData.password,
      };

      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(registerPayload),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'Failed to create user');
      }

      const result = await response.json();
      console.log('User created:', result);
      setSuccess(true);

      // Store user data in localStorage
      if (result.data) {
        localStorage.setItem('user', JSON.stringify(result.data));
        onRegister(result.data);
      }

      // Redirect to home page after 1.5 seconds
      setTimeout(() => {
        navigate('/');
      }, 1500);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'An error occurred');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
      <Paper
        elevation={3}
        sx={{
          p: 4,
          borderTop: `4px solid ${colors.brightTealBlue}`,
        }}
      >
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
          <PersonAddIcon
            sx={{ fontSize: 40, mr: 2, color: colors.deepTwilight }}
          />
          <Typography variant="h4" component="h1" sx={{ color: colors.deepTwilight }}>
            Create New User
          </Typography>
        </Box>

        {error && (
          <Alert severity="error" sx={{ mb: 3 }} onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {success && (
          <Alert severity="success" sx={{ mb: 3 }} onClose={() => setSuccess(false)}>
            User created successfully!
          </Alert>
        )}

        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            {/* Required Fields */}
            <Grid item xs={12}>
              <Typography
                variant="h6"
                sx={{ color: colors.brightTealBlue, mb: 2 }}
              >
                Required Information
              </Typography>
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                label="Name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                variant="outlined"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                label="Email"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleChange}
                variant="outlined"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                label="Password"
                name="password"
                type="password"
                value={formData.password}
                onChange={handleChange}
                variant="outlined"
                helperText="Must be at least 8 characters"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                required
                fullWidth
                label="Confirm Password"
                name="confirmPassword"
                type="password"
                value={formData.confirmPassword}
                onChange={handleChange}
                variant="outlined"
                error={formData.confirmPassword !== '' && formData.password !== formData.confirmPassword}
                helperText={
                  formData.confirmPassword !== '' && formData.password !== formData.confirmPassword
                    ? 'Passwords do not match'
                    : ''
                }
              />
            </Grid>

            {/* Optional Fields */}
            <Grid item xs={12}>
              <Typography
                variant="h6"
                sx={{ color: colors.brightTealBlue, mb: 2, mt: 2 }}
              >
                Additional Information
              </Typography>
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Phone Number"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                variant="outlined"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Location"
                name="location"
                value={formData.location}
                onChange={handleChange}
                variant="outlined"
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                select
                label="Education Level"
                name="educationLevel"
                value={formData.educationLevel}
                onChange={handleChange}
                variant="outlined"
              >
                {educationLevels.map((level) => (
                  <MenuItem key={level} value={level}>
                    {level}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                select
                label="Employment Status"
                name="employmentStatus"
                value={formData.employmentStatus}
                onChange={handleChange}
                variant="outlined"
              >
                {employmentStatuses.map((status) => (
                  <MenuItem key={status} value={status}>
                    {status}
                  </MenuItem>
                ))}
              </TextField>
            </Grid>

            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Summary"
                name="summary"
                value={formData.summary}
                onChange={handleChange}
                variant="outlined"
                multiline
                rows={4}
                placeholder="Tell us about yourself..."
              />
            </Grid>

            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Profile Picture URL"
                name="profilePictureUrl"
                value={formData.profilePictureUrl}
                onChange={handleChange}
                variant="outlined"
                placeholder="https://example.com/profile.jpg"
              />
            </Grid>

            <Grid item xs={12}>
              <Typography
                variant="h6"
                sx={{ color: colors.brightTealBlue, mb: 2 }}
              >
                Salary Expectations
              </Typography>
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Minimum Salary"
                name="salaryExpectationsMin"
                type="number"
                value={formData.salaryExpectationsMin}
                onChange={handleChange}
                variant="outlined"
                InputProps={{
                  startAdornment: <Typography sx={{ mr: 1 }}>$</Typography>,
                }}
              />
            </Grid>

            <Grid item xs={12} sm={6}>
              <TextField
                fullWidth
                label="Maximum Salary"
                name="salaryExpectationsMax"
                type="number"
                value={formData.salaryExpectationsMax}
                onChange={handleChange}
                variant="outlined"
                InputProps={{
                  startAdornment: <Typography sx={{ mr: 1 }}>$</Typography>,
                }}
              />
            </Grid>

            <Grid item xs={12}>
              <FormControlLabel
                control={
                  <Checkbox
                    checked={formData.activelySeeking}
                    onChange={handleChange}
                    name="activelySeeking"
                    sx={{
                      color: colors.brightTealBlue,
                      '&.Mui-checked': {
                        color: colors.deepTwilight,
                      },
                    }}
                  />
                }
                label="Actively seeking employment"
              />
            </Grid>

            {/* Submit Button */}
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end', mt: 2 }}>
                <Button
                  type="button"
                  variant="outlined"
                  onClick={() => {
                    setFormData({
                      name: '',
                      email: '',
                      password: '',
                      confirmPassword: '',
                      phoneNumber: '',
                      location: '',
                      educationLevel: '',
                      summary: '',
                      profilePictureUrl: '',
                      employmentStatus: '',
                      salaryExpectationsMin: '',
                      salaryExpectationsMax: '',
                      activelySeeking: false,
                    });
                  }}
                  sx={{
                    borderColor: colors.brightTealBlue,
                    color: colors.brightTealBlue,
                    '&:hover': {
                      borderColor: colors.deepTwilight,
                      backgroundColor: colors.lightCyan,
                    },
                  }}
                >
                  Reset
                </Button>
                <Button
                  type="submit"
                  variant="contained"
                  disabled={loading}
                  sx={{
                    backgroundColor: colors.brightTealBlue,
                    '&:hover': {
                      backgroundColor: colors.deepTwilight,
                    },
                  }}
                >
                  {loading ? <CircularProgress size={24} /> : 'Create User'}
                </Button>
              </Box>
            </Grid>
          </Grid>
        </form>

        <Divider sx={{ my: 3 }} />

        <Box sx={{ textAlign: 'center' }}>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
            Already have an account?
          </Typography>
          <Button
            component={RouterLink}
            to="/login"
            variant="outlined"
            startIcon={<LoginIcon />}
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
            Login to Existing Account
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

export default CreateUser;

