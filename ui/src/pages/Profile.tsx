import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Paper,
  Typography,
  Box,
  Button,
  Grid,
  Avatar,
  Divider,
  Chip,
  CircularProgress,
  TextField,
  MenuItem,
  FormControlLabel,
  Checkbox,
  Alert,
  Tabs,
  Tab,
} from '@mui/material';
import {
  Email as EmailIcon,
  Phone as PhoneIcon,
  LocationOn as LocationIcon,
  School as SchoolIcon,
  Work as WorkIcon,
  AttachMoney as MoneyIcon,
  Logout as LogoutIcon,
  Edit as EditIcon,
  Save as SaveIcon,
  Cancel as CancelIcon,
  History as HistoryIcon,
  PersonSearch as RecruitingIcon,
  ContactMail as ContactIcon,
} from '@mui/icons-material';
import { colors } from '../theme';

interface User {
  id: number;
  name: string;
  email: string;
  phoneNumber?: string;
  location?: string;
  educationLevel?: string;
  summary?: string;
  profilePictureUrl?: string;
  employmentStatus?: string;
  salaryExpectationsMin?: number;
  salaryExpectationsMax?: number;
  activelySeeking?: boolean;
  lastLoginAt?: string;
  createdAt?: string;
}

interface ProfileProps {
  user: User | null;
  onLogout: () => void;
  onUpdateUser: (user: User) => void;
}

const Profile: React.FC<ProfileProps> = ({ user, onLogout, onUpdateUser }) => {
  const navigate = useNavigate();
  const [isEditing, setIsEditing] = useState(false);
  const [editedUser, setEditedUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [currentTab, setCurrentTab] = useState(0);
  const [workHistory, setWorkHistory] = useState<any[]>([]);
  const [newWorkEntry, setNewWorkEntry] = useState({
    company: '',
    title: '',
    startDate: '',
    endDate: '',
    description: '',
    current: false,
  });

  useEffect(() => {
    // If no user is logged in, redirect to login
    if (!user) {
      navigate('/login');
    } else {
      setEditedUser(user);
    }
  }, [user, navigate]);

  const handleLogout = () => {
    onLogout();
    navigate('/');
  };

  const handleEdit = () => {
    setIsEditing(true);
    setEditedUser({ ...user! });
    setError(null);
    setSuccess(null);
  };

  const handleCancel = () => {
    setIsEditing(false);
    setEditedUser(user);
    setError(null);
    setSuccess(null);
  };

  const handleSave = async () => {
    if (!editedUser) return;

    setLoading(true);
    setError(null);
    setSuccess(null);

    try {
      const response = await fetch(`http://localhost:8080/api/users/${editedUser.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(editedUser),
      });

      const result = await response.json();

      if (!response.ok || !result.success) {
        throw new Error(result.message || 'Failed to update profile');
      }

      // Update localStorage with new user data
      localStorage.setItem('user', JSON.stringify(result.data));

      // Update parent component's user state
      onUpdateUser(result.data);

      setSuccess('Profile updated successfully!');
      setIsEditing(false);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to update profile');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (field: keyof User, value: any) => {
    if (!editedUser) return;
    setEditedUser({
      ...editedUser,
      [field]: value,
    });
  };

  const handleAddWorkEntry = () => {
    if (newWorkEntry.company && newWorkEntry.title) {
      setWorkHistory([...workHistory, { ...newWorkEntry, id: Date.now() }]);
      setNewWorkEntry({
        company: '',
        title: '',
        startDate: '',
        endDate: '',
        description: '',
        current: false,
      });
    }
  };

  const handleRemoveWorkEntry = (id: number) => {
    setWorkHistory(workHistory.filter(entry => entry.id !== id));
  };

  const handleWorkEntryChange = (field: string, value: any) => {
    setNewWorkEntry({
      ...newWorkEntry,
      [field]: value,
    });
  };

  if (!user || !editedUser) {
    return (
      <Container maxWidth="md" sx={{ mt: 8 }}>
        <CircularProgress />
      </Container>
    );
  }

  return (
    <Container maxWidth="md" sx={{ mt: 8, mb: 4 }}>
      <Paper
        elevation={3}
        sx={{
          p: 4,
          borderTop: `4px solid ${colors.brightTealBlue}`,
        }}
      >
        {/* Success/Error Messages */}
        {success && (
          <Alert severity="success" sx={{ mb: 3 }} onClose={() => setSuccess(null)}>
            {success}
          </Alert>
        )}
        {error && (
          <Alert severity="error" sx={{ mb: 3 }} onClose={() => setError(null)}>
            {error}
          </Alert>
        )}

        {/* Header Section */}
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 4 }}>
          <Avatar
            src={user.profilePictureUrl}
            sx={{
              width: 100,
              height: 100,
              mr: 3,
              bgcolor: colors.brightTealBlue,
              fontSize: 40,
            }}
          >
            {user.name.charAt(0).toUpperCase()}
          </Avatar>
          <Box sx={{ flexGrow: 1 }}>
            <Typography variant="h4" sx={{ color: colors.deepTwilight, mb: 1 }}>
              {user.name}
            </Typography>
            <Typography variant="body1" color="text.secondary">
              Member since {user.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A'}
            </Typography>
            {user.activelySeeking && (
              <Chip
                label="Actively Seeking"
                color="success"
                size="small"
                sx={{ mt: 1 }}
              />
            )}
          </Box>
          <Box sx={{ display: 'flex', gap: 1 }}>
            {!isEditing ? (
              <>
                <Button
                  variant="contained"
                  startIcon={<EditIcon />}
                  onClick={handleEdit}
                  sx={{
                    bgcolor: colors.brightTealBlue,
                    '&:hover': {
                      bgcolor: colors.deepTwilight,
                    },
                  }}
                >
                  Edit Profile
                </Button>
                <Button
                  variant="outlined"
                  startIcon={<LogoutIcon />}
                  onClick={handleLogout}
                  sx={{
                    borderColor: colors.brightTealBlue,
                    color: colors.brightTealBlue,
                    '&:hover': {
                      borderColor: colors.deepTwilight,
                      backgroundColor: colors.lightCyan,
                    },
                  }}
                >
                  Logout
                </Button>
              </>
            ) : (
              <>
                <Button
                  variant="contained"
                  color="success"
                  startIcon={<SaveIcon />}
                  onClick={handleSave}
                  disabled={loading}
                >
                  {loading ? 'Saving...' : 'Save'}
                </Button>
                <Button
                  variant="outlined"
                  color="error"
                  startIcon={<CancelIcon />}
                  onClick={handleCancel}
                  disabled={loading}
                >
                  Cancel
                </Button>
              </>
            )}
          </Box>
        </Box>

        {/* Tabs */}
        <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: 3 }}>
          <Tabs
            value={currentTab}
            onChange={(_, newValue) => setCurrentTab(newValue)}
            sx={{
              '& .MuiTab-root': {
                color: colors.deepTwilight,
              },
              '& .Mui-selected': {
                color: colors.brightTealBlue,
              },
              '& .MuiTabs-indicator': {
                backgroundColor: colors.brightTealBlue,
              },
            }}
          >
            <Tab icon={<HistoryIcon />} label="Work History" iconPosition="start" />
            <Tab icon={<RecruitingIcon />} label="Recruiting Information" iconPosition="start" />
            <Tab icon={<ContactIcon />} label="Contact Information" iconPosition="start" />
          </Tabs>
        </Box>

        {/* Tab Content */}
        {currentTab === 0 && (
          <Box>
            {/* Education Section */}
            <Typography variant="h6" sx={{ color: colors.brightTealBlue, mb: 2 }}>
              Education
            </Typography>
            <Grid container spacing={2} sx={{ mb: 4 }}>
              <Grid item xs={12} sm={6}>
                {isEditing ? (
                  <TextField
                    fullWidth
                    select
                    label="Education Level"
                    value={editedUser.educationLevel || ''}
                    onChange={(e) => handleInputChange('educationLevel', e.target.value)}
                  >
                    <MenuItem value="">None</MenuItem>
                    <MenuItem value="High School">High School</MenuItem>
                    <MenuItem value="Associate's Degree">Associate's Degree</MenuItem>
                    <MenuItem value="Bachelor's Degree">Bachelor's Degree</MenuItem>
                    <MenuItem value="Master's Degree">Master's Degree</MenuItem>
                    <MenuItem value="Doctorate">Doctorate</MenuItem>
                    <MenuItem value="Other">Other</MenuItem>
                  </TextField>
                ) : (
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <SchoolIcon sx={{ mr: 2, color: colors.turquoiseSurf }} />
                    <Box>
                      <Typography variant="caption" color="text.secondary">
                        Education Level
                      </Typography>
                      <Typography variant="body1" sx={{ fontStyle: user.educationLevel ? 'normal' : 'italic', color: user.educationLevel ? 'inherit' : 'text.secondary' }}>
                        {user.educationLevel || 'No information provided'}
                      </Typography>
                    </Box>
                  </Box>
                )}
              </Grid>
            </Grid>

            <Divider sx={{ mb: 3 }} />

            {/* Work History Section */}
            <Typography variant="h6" sx={{ color: colors.brightTealBlue, mb: 2 }}>
              Work History
            </Typography>

            {/* Existing Work History Entries */}
            {workHistory.length > 0 ? (
              <Box sx={{ mb: 3 }}>
                {workHistory
                  .sort((a, b) => {
                    // Sort by start date, most recent first
                    if (a.current) return -1;
                    if (b.current) return 1;
                    return new Date(b.startDate).getTime() - new Date(a.startDate).getTime();
                  })
                  .map((entry) => (
                    <Paper
                      key={entry.id}
                      elevation={2}
                      sx={{
                        p: 3,
                        mb: 2,
                        borderLeft: `4px solid ${colors.turquoiseSurf}`,
                      }}
                    >
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                        <Box sx={{ flexGrow: 1 }}>
                          <Typography variant="h6" sx={{ color: colors.deepTwilight, mb: 0.5 }}>
                            {entry.title}
                          </Typography>
                          <Typography variant="subtitle1" sx={{ color: colors.brightTealBlue, mb: 1 }}>
                            {entry.company}
                          </Typography>
                          <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                            {entry.startDate} - {entry.current ? 'Present' : entry.endDate}
                          </Typography>
                          {entry.description && (
                            <Typography variant="body2" color="text.secondary">
                              {entry.description}
                            </Typography>
                          )}
                        </Box>
                        {isEditing && (
                          <Button
                            variant="outlined"
                            color="error"
                            size="small"
                            onClick={() => handleRemoveWorkEntry(entry.id)}
                          >
                            Remove
                          </Button>
                        )}
                      </Box>
                    </Paper>
                  ))}
              </Box>
            ) : (
              <Typography variant="body2" color="text.secondary" sx={{ mb: 3, fontStyle: 'italic' }}>
                No work history added yet.
              </Typography>
            )}

            {/* Add New Work Entry Form (only in edit mode) */}
            {isEditing && (
              <Paper
                elevation={3}
                sx={{
                  p: 3,
                  backgroundColor: colors.lightCyan,
                  borderTop: `3px solid ${colors.brightTealBlue}`,
                }}
              >
                <Typography variant="h6" sx={{ color: colors.deepTwilight, mb: 2 }}>
                  Add Work Experience
                </Typography>
                <Grid container spacing={2}>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Company"
                      value={newWorkEntry.company}
                      onChange={(e) => handleWorkEntryChange('company', e.target.value)}
                      required
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Job Title"
                      value={newWorkEntry.title}
                      onChange={(e) => handleWorkEntryChange('title', e.target.value)}
                      required
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Start Date"
                      type="month"
                      value={newWorkEntry.startDate}
                      onChange={(e) => handleWorkEntryChange('startDate', e.target.value)}
                      InputLabelProps={{ shrink: true }}
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="End Date"
                      type="month"
                      value={newWorkEntry.endDate}
                      onChange={(e) => handleWorkEntryChange('endDate', e.target.value)}
                      disabled={newWorkEntry.current}
                      InputLabelProps={{ shrink: true }}
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <FormControlLabel
                      control={
                        <Checkbox
                          checked={newWorkEntry.current}
                          onChange={(e) => handleWorkEntryChange('current', e.target.checked)}
                        />
                      }
                      label="I currently work here"
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      multiline
                      rows={3}
                      label="Description"
                      value={newWorkEntry.description}
                      onChange={(e) => handleWorkEntryChange('description', e.target.value)}
                      placeholder="Describe your responsibilities and achievements..."
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <Button
                      variant="contained"
                      onClick={handleAddWorkEntry}
                      disabled={!newWorkEntry.company || !newWorkEntry.title}
                      sx={{
                        bgcolor: colors.brightTealBlue,
                        '&:hover': {
                          bgcolor: colors.deepTwilight,
                        },
                      }}
                    >
                      Add Work Experience
                    </Button>
                  </Grid>
                </Grid>
              </Paper>
            )}
          </Box>
        )}

        {currentTab === 1 && (
          <Box>
            <Typography variant="h6" sx={{ color: colors.brightTealBlue, mb: 3 }}>
              Recruiting Information
            </Typography>
            <Grid container spacing={2} sx={{ mb: 3 }}>
              <Grid item xs={12} sm={6}>
                {isEditing ? (
                  <TextField
                    fullWidth
                    select
                    label="Employment Status"
                    value={editedUser.employmentStatus || ''}
                    onChange={(e) => handleInputChange('employmentStatus', e.target.value)}
                  >
                    <MenuItem value="">None</MenuItem>
                    <MenuItem value="Employed">Employed</MenuItem>
                    <MenuItem value="Unemployed">Unemployed</MenuItem>
                    <MenuItem value="Self-Employed">Self-Employed</MenuItem>
                    <MenuItem value="Student">Student</MenuItem>
                    <MenuItem value="Retired">Retired</MenuItem>
                  </TextField>
                ) : (
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <WorkIcon sx={{ mr: 2, color: colors.turquoiseSurf }} />
                    <Box>
                      <Typography variant="caption" color="text.secondary">
                        Employment Status
                      </Typography>
                      <Typography variant="body1" sx={{ fontStyle: user.employmentStatus ? 'normal' : 'italic', color: user.employmentStatus ? 'inherit' : 'text.secondary' }}>
                        {user.employmentStatus || 'No information provided'}
                      </Typography>
                    </Box>
                  </Box>
                )}
              </Grid>
              <Grid item xs={12} sm={6}>
                {isEditing ? (
                  <TextField
                    fullWidth
                    label="Minimum Salary Expectation"
                    type="number"
                    value={editedUser.salaryExpectationsMin || ''}
                    onChange={(e) => handleInputChange('salaryExpectationsMin', e.target.value ? Number(e.target.value) : null)}
                  />
                ) : (
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <MoneyIcon sx={{ mr: 2, color: colors.turquoiseSurf }} />
                    <Box>
                      <Typography variant="caption" color="text.secondary">
                        Salary Expectations
                      </Typography>
                      <Typography variant="body1" sx={{ fontStyle: (user.salaryExpectationsMin || user.salaryExpectationsMax) ? 'normal' : 'italic', color: (user.salaryExpectationsMin || user.salaryExpectationsMax) ? 'inherit' : 'text.secondary' }}>
                        {(user.salaryExpectationsMin || user.salaryExpectationsMax)
                          ? `$${user.salaryExpectationsMin?.toLocaleString() || '0'} - $${user.salaryExpectationsMax?.toLocaleString() || '0'}`
                          : 'No information provided'}
                      </Typography>
                    </Box>
                  </Box>
                )}
              </Grid>
              <Grid item xs={12} sm={6}>
                {isEditing && (
                  <TextField
                    fullWidth
                    label="Maximum Salary Expectation"
                    type="number"
                    value={editedUser.salaryExpectationsMax || ''}
                    onChange={(e) => handleInputChange('salaryExpectationsMax', e.target.value ? Number(e.target.value) : null)}
                  />
                )}
              </Grid>
              <Grid item xs={12}>
                {isEditing && (
                  <FormControlLabel
                    control={
                      <Checkbox
                        checked={editedUser.activelySeeking || false}
                        onChange={(e) => handleInputChange('activelySeeking', e.target.checked)}
                      />
                    }
                    label="Actively Seeking Opportunities"
                  />
                )}
              </Grid>
            </Grid>

            <Divider sx={{ mb: 3 }} />
            <Typography variant="h6" sx={{ color: colors.brightTealBlue, mb: 2 }}>
              About
            </Typography>
            {isEditing ? (
              <TextField
                fullWidth
                multiline
                rows={4}
                label="Summary"
                value={editedUser.summary || ''}
                onChange={(e) => handleInputChange('summary', e.target.value)}
                placeholder="Tell us about yourself..."
              />
            ) : (
              <Typography variant="body1" color="text.secondary" paragraph sx={{ fontStyle: user.summary ? 'normal' : 'italic' }}>
                {user.summary || 'No information provided'}
              </Typography>
            )}
          </Box>
        )}

        {currentTab === 2 && (
          <Box>
            <Typography variant="h6" sx={{ color: colors.brightTealBlue, mb: 3 }}>
              Contact Information
            </Typography>
            <Grid container spacing={2} sx={{ mb: 3 }}>
              <Grid item xs={12} sm={6}>
                {isEditing ? (
                  <TextField
                    fullWidth
                    label="Name"
                    value={editedUser.name}
                    onChange={(e) => handleInputChange('name', e.target.value)}
                    required
                  />
                ) : (
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <EmailIcon sx={{ mr: 2, color: colors.turquoiseSurf }} />
                    <Box>
                      <Typography variant="caption" color="text.secondary">
                        Name
                      </Typography>
                      <Typography variant="body1">{user.name}</Typography>
                    </Box>
                  </Box>
                )}
              </Grid>
              <Grid item xs={12} sm={6}>
                {isEditing ? (
                  <TextField
                    fullWidth
                    label="Email"
                    type="email"
                    value={editedUser.email}
                    onChange={(e) => handleInputChange('email', e.target.value)}
                    required
                  />
                ) : (
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <EmailIcon sx={{ mr: 2, color: colors.turquoiseSurf }} />
                    <Box>
                      <Typography variant="caption" color="text.secondary">
                        Email
                      </Typography>
                      <Typography variant="body1">{user.email}</Typography>
                    </Box>
                  </Box>
                )}
              </Grid>
              <Grid item xs={12} sm={6}>
                {isEditing ? (
                  <TextField
                    fullWidth
                    label="Phone Number"
                    value={editedUser.phoneNumber || ''}
                    onChange={(e) => handleInputChange('phoneNumber', e.target.value)}
                  />
                ) : (
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <PhoneIcon sx={{ mr: 2, color: colors.turquoiseSurf }} />
                    <Box>
                      <Typography variant="caption" color="text.secondary">
                        Phone
                      </Typography>
                      <Typography variant="body1" sx={{ fontStyle: user.phoneNumber ? 'normal' : 'italic', color: user.phoneNumber ? 'inherit' : 'text.secondary' }}>
                        {user.phoneNumber || 'No information provided'}
                      </Typography>
                    </Box>
                  </Box>
                )}
              </Grid>
              <Grid item xs={12} sm={6}>
                {isEditing ? (
                  <TextField
                    fullWidth
                    label="Location"
                    value={editedUser.location || ''}
                    onChange={(e) => handleInputChange('location', e.target.value)}
                  />
                ) : (
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <LocationIcon sx={{ mr: 2, color: colors.turquoiseSurf }} />
                    <Box>
                      <Typography variant="caption" color="text.secondary">
                        Location
                      </Typography>
                      <Typography variant="body1" sx={{ fontStyle: user.location ? 'normal' : 'italic', color: user.location ? 'inherit' : 'text.secondary' }}>
                        {user.location || 'No information provided'}
                      </Typography>
                    </Box>
                  </Box>
                )}
              </Grid>
              <Grid item xs={12} sm={6}>
                {isEditing ? (
                  <TextField
                    fullWidth
                    label="Profile Picture URL"
                    value={editedUser.profilePictureUrl || ''}
                    onChange={(e) => handleInputChange('profilePictureUrl', e.target.value)}
                  />
                ) : null}
              </Grid>
            </Grid>

            <Divider sx={{ my: 3 }} />

            {/* Last Login */}
            {user.lastLoginAt && (
              <Typography variant="caption" color="text.secondary">
                Last login: {new Date(user.lastLoginAt).toLocaleString()}
              </Typography>
            )}
          </Box>
        )}
      </Paper>
    </Container>
  );
};

export default Profile;

