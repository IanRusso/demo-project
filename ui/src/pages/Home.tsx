import React, { useState, useEffect } from 'react';
import {
  Container,
  Grid,
  Paper,
  Typography,
  Box,
  TextField,
  Chip,
  Card,
  CardContent,
  CardActions,
  Button,
  Avatar,
  CircularProgress,
  Alert,
  InputAdornment,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  IconButton,
  Badge,
  FormControlLabel,
  Checkbox,
  Divider,
} from '@mui/material';
import {
  Search as SearchIcon,
  Work as WorkIcon,
  LocationOn as LocationIcon,
  AttachMoney as MoneyIcon,
  Business as BusinessIcon,
  Person as PersonIcon,
  CalendarToday as CalendarIcon,
  FilterList as FilterIcon,
  Clear as ClearIcon,
  Close as CloseIcon,
} from '@mui/icons-material';
import { colors } from '../theme';

interface User {
  id: number;
  name: string;
  email: string;
  location?: string;
  profile_picture_url?: string;
}

interface JobPosting {
  id: number;
  employer_id: number;
  title: string;
  description: string;
  location?: string;
  field?: string;
  experience_level?: string;
  salary_min?: number;
  salary_max?: number;
  status: string;
  posted_date: number;
}

interface UserExperience {
  id: number;
  user_id: number;
  title: string;
  description: string;
  experience_type?: string;
  start_date?: string;
  end_date?: string;
  is_current?: boolean;
  created_at: number;
  updated_at: number;
}

interface Employer {
  id: number;
  name: string;
  location?: string;
  company_picture_url?: string;
}

interface UserConnection {
  user_id: number;
  connected_user_id: number;
}

interface HomeProps {
  user: User | null;
}

interface FeedItem {
  type: 'job' | 'experience';
  timestamp: number;
  data: JobPosting | UserExperience;
}

const Home: React.FC<HomeProps> = ({ user }) => {
  const [jobPostings, setJobPostings] = useState<JobPosting[]>([]);
  const [experiences, setExperiences] = useState<UserExperience[]>([]);
  const [employers, setEmployers] = useState<Map<number, Employer>>(new Map());
  const [experienceUsers, setExperienceUsers] = useState<Map<number, User>>(new Map());
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Filter states
  const [searchQuery, setSearchQuery] = useState('');
  const [locationFilter, setLocationFilter] = useState('');
  const [professionFilter, setProfessionFilter] = useState('');
  const [companyFilter, setCompanyFilter] = useState('');
  const [skillFilter, setSkillFilter] = useState('');
  const [filterDialogOpen, setFilterDialogOpen] = useState(false);
  const [showJobPostings, setShowJobPostings] = useState(true);
  const [showExperiences, setShowExperiences] = useState(true);

  useEffect(() => {
    fetchData();
  }, [user]);

  const fetchData = async () => {
    setLoading(true);
    setError(null);

    try {
      // Fetch job postings
      const jobsResponse = await fetch('http://localhost:8080/api/job-postings');
      const jobsResult = await jobsResponse.json();
      
      if (jobsResult.success && Array.isArray(jobsResult.data)) {
        const activeJobs = jobsResult.data.filter((job: JobPosting) => job.status === 'ACTIVE');
        setJobPostings(activeJobs);

        // Fetch employer details for each job
        const employerIds = [...new Set(activeJobs.map((job: JobPosting) => job.employer_id))] as number[];
        const employerMap = new Map<number, Employer>();

        await Promise.all(
          employerIds.map(async (employerId: number) => {
            try {
              const empResponse = await fetch(`http://localhost:8080/api/employers/${employerId}`);
              const empResult = await empResponse.json();
              if (empResult.success && empResult.data) {
                employerMap.set(employerId, empResult.data);
              }
            } catch (err) {
              console.error(`Error fetching employer ${employerId}:`, err);
            }
          })
        );
        setEmployers(employerMap);
      }

      // Fetch experiences from connected users
      if (user) {
        const connectionsResponse = await fetch(
          `http://localhost:8080/api/user-connections/user/${user.id}/accepted`
        );
        const connectionsResult = await connectionsResponse.json();

        if (connectionsResult.success && Array.isArray(connectionsResult.data)) {
          const connections: UserConnection[] = connectionsResult.data;
          const connectedUserIds = connections.map((conn) =>
            conn.user_id === user.id ? conn.connected_user_id : conn.user_id
          );

          // Fetch experiences for all connected users
          const allExperiences: UserExperience[] = [];
          const userMap = new Map<number, User>();

          await Promise.all(
            connectedUserIds.map(async (userId) => {
              try {
                const expResponse = await fetch(
                  `http://localhost:8080/api/user-experiences/user/${userId}`
                );
                const expResult = await expResponse.json();
                
                if (expResult.success && Array.isArray(expResult.data)) {
                  allExperiences.push(...expResult.data);
                }

                // Fetch user details
                const userResponse = await fetch(`http://localhost:8080/api/users/${userId}`);
                const userResult = await userResponse.json();
                if (userResult.success && userResult.data) {
                  userMap.set(userId, userResult.data);
                }
              } catch (err) {
                console.error(`Error fetching data for user ${userId}:`, err);
              }
            })
          );

          // Sort by recency (created_at or updated_at)
          allExperiences.sort((a, b) => b.updated_at - a.updated_at);
          setExperiences(allExperiences);
          setExperienceUsers(userMap);
        }
      }
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  const formatSalary = (min?: number, max?: number) => {
    if (!min && !max) return 'Not specified';
    if (min && max) return `$${min.toLocaleString()} - $${max.toLocaleString()}`;
    if (min) return `$${min.toLocaleString()}+`;
    return `Up to $${max?.toLocaleString()}`;
  };

  const formatDate = (timestamp: number) => {
    return new Date(timestamp).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  const formatMonthYear = (dateValue: string | number | undefined) => {
    if (!dateValue) return '';

    // If it's a number (timestamp), convert it
    if (typeof dateValue === 'number') {
      return new Date(dateValue).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
      });
    }

    // If it's a string that looks like a timestamp
    const numValue = Number(dateValue);
    if (!isNaN(numValue) && numValue > 1000000000) {
      return new Date(numValue).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
      });
    }

    // If it's already a formatted string (YYYY-MM format), parse and format it
    if (typeof dateValue === 'string' && dateValue.includes('-')) {
      const [year, month] = dateValue.split('-');
      const date = new Date(parseInt(year), parseInt(month) - 1);
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
      });
    }

    return dateValue.toString();
  };



  const clearFilters = () => {
    setLocationFilter('');
    setProfessionFilter('');
    setCompanyFilter('');
    setSkillFilter('');
    setShowJobPostings(true);
    setShowExperiences(true);
  };

  const getActiveFilterCount = () => {
    let count = 0;
    if (locationFilter) count++;
    if (professionFilter) count++;
    if (companyFilter) count++;
    if (skillFilter) count++;
    if (!showJobPostings || !showExperiences) count++;
    return count;
  };

  const hasActiveFilters = getActiveFilterCount() > 0;

  const getCombinedFeed = (): FeedItem[] => {
    const feed: FeedItem[] = [];

    // Add job postings to feed
    if (showJobPostings) {
      jobPostings.forEach((job) => {
        feed.push({
          type: 'job',
          timestamp: job.posted_date,
          data: job,
        });
      });
    }

    // Add experiences to feed
    if (showExperiences) {
      experiences.forEach((exp) => {
        feed.push({
          type: 'experience',
          timestamp: exp.updated_at,
          data: exp,
        });
      });
    }

    // Sort by timestamp (most recent first)
    feed.sort((a, b) => b.timestamp - a.timestamp);

    return feed;
  };

  const getFilteredFeed = () => {
    return getCombinedFeed().filter((item) => {
      if (item.type === 'job') {
        const job = item.data as JobPosting;
        const matchesSearch =
          !searchQuery ||
          job.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
          job.description.toLowerCase().includes(searchQuery.toLowerCase());

        const matchesLocation =
          !locationFilter ||
          (job.location && job.location.toLowerCase().includes(locationFilter.toLowerCase()));

        const matchesCompany =
          !companyFilter ||
          (employers.get(job.employer_id)?.name || '')
            .toLowerCase()
            .includes(companyFilter.toLowerCase());

        return matchesSearch && matchesLocation && matchesCompany;
      } else {
        const exp = item.data as UserExperience;
        const matchesSearch =
          !searchQuery ||
          exp.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
          exp.description.toLowerCase().includes(searchQuery.toLowerCase());

        const matchesLocation =
          !locationFilter ||
          (experienceUsers.get(exp.user_id)?.location || '')
            .toLowerCase()
            .includes(locationFilter.toLowerCase());

        const matchesSkill =
          !skillFilter ||
          exp.description.toLowerCase().includes(skillFilter.toLowerCase());

        return matchesSearch && matchesLocation && matchesSkill;
      }
    });
  };

  if (loading) {
    return (
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4, textAlign: 'center' }}>
        <CircularProgress size={60} />
        <Typography variant="h6" sx={{ mt: 2 }}>
          Loading feed...
        </Typography>
      </Container>
    );
  }

  if (error) {
    return (
      <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
        <Alert severity="error">{error}</Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Typography variant="h4" gutterBottom sx={{ color: colors.deepTwilight, fontWeight: 'bold' }}>
        {user ? `Welcome back, ${user.name}!` : 'Welcome to Gainfully'}
      </Typography>

      {/* Search Bar with Filter Button */}
      <Box sx={{ mb: 3, display: 'flex', gap: 2, alignItems: 'center' }}>
        <TextField
          fullWidth
          placeholder="Search by title or description..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <SearchIcon />
              </InputAdornment>
            ),
          }}
          sx={{
            '& .MuiOutlinedInput-root': {
              backgroundColor: 'white',
            },
          }}
        />
        <Badge badgeContent={getActiveFilterCount()} color="error">
          <Button
            variant="contained"
            startIcon={<FilterIcon />}
            onClick={() => setFilterDialogOpen(true)}
            sx={{
              minWidth: '120px',
              height: '56px',
              bgcolor: colors.brightTealBlue,
              color: 'white',
              fontWeight: 'bold',
              '&:hover': {
                bgcolor: colors.deepTwilight,
              },
            }}
          >
            Filters
          </Button>
        </Badge>
      </Box>

      {/* Filter Dialog */}
      <Dialog
        open={filterDialogOpen}
        onClose={() => setFilterDialogOpen(false)}
        maxWidth="sm"
        fullWidth
      >
        <DialogTitle>
          <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <FilterIcon sx={{ mr: 1, color: colors.deepTwilight }} />
              <Typography variant="h6" sx={{ color: colors.deepTwilight }}>
                Filters
              </Typography>
            </Box>
            <IconButton onClick={() => setFilterDialogOpen(false)} size="small">
              <CloseIcon />
            </IconButton>
          </Box>
        </DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            {/* Content Type Filters */}
            <Grid item xs={12}>
              <Typography variant="subtitle2" sx={{ mb: 1, color: colors.deepTwilight, fontWeight: 'bold' }}>
                Show Content
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 0.5 }}>
                <FormControlLabel
                  control={
                    <Checkbox
                      checked={showJobPostings}
                      onChange={(e) => setShowJobPostings(e.target.checked)}
                      sx={{
                        color: colors.brightTealBlue,
                        '&.Mui-checked': {
                          color: colors.brightTealBlue,
                        },
                      }}
                    />
                  }
                  label={
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <WorkIcon sx={{ mr: 1, fontSize: 20 }} />
                      Job Postings
                    </Box>
                  }
                />
                <FormControlLabel
                  control={
                    <Checkbox
                      checked={showExperiences}
                      onChange={(e) => setShowExperiences(e.target.checked)}
                      sx={{
                        color: colors.brightTealBlue,
                        '&.Mui-checked': {
                          color: colors.brightTealBlue,
                        },
                      }}
                    />
                  }
                  label={
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <PersonIcon sx={{ mr: 1, fontSize: 20 }} />
                      Experiences from Connections
                    </Box>
                  }
                />
              </Box>
            </Grid>

            <Grid item xs={12}>
              <Divider />
            </Grid>

            {/* Location Filter */}
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Location"
                placeholder="Filter by location..."
                value={locationFilter}
                onChange={(e) => setLocationFilter(e.target.value)}
                InputProps={{
                  startAdornment: (
                    <InputAdornment position="start">
                      <LocationIcon />
                    </InputAdornment>
                  ),
                }}
              />
            </Grid>

            {/* Company Filter (for job postings) */}
            {showJobPostings && (
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Company"
                  placeholder="Filter by company..."
                  value={companyFilter}
                  onChange={(e) => setCompanyFilter(e.target.value)}
                  InputProps={{
                    startAdornment: (
                      <InputAdornment position="start">
                        <BusinessIcon />
                      </InputAdornment>
                    ),
                  }}
                />
              </Grid>
            )}

            {/* Skills Filter (for experiences) */}
            {showExperiences && (
              <Grid item xs={12}>
                <TextField
                  fullWidth
                  label="Skills"
                  placeholder="Filter by skills mentioned..."
                  value={skillFilter}
                  onChange={(e) => setSkillFilter(e.target.value)}
                />
              </Grid>
            )}
          </Grid>
        </DialogContent>
        <DialogActions sx={{ px: 3, pb: 2 }}>
          <Button onClick={clearFilters} startIcon={<ClearIcon />}>
            Clear All
          </Button>
          <Button
            onClick={() => setFilterDialogOpen(false)}
            variant="contained"
            sx={{ bgcolor: colors.brightTealBlue }}
          >
            Apply Filters
          </Button>
        </DialogActions>
      </Dialog>

      {/* Unified Feed */}
      <Box>
        {getFilteredFeed().length === 0 ? (
          <Paper sx={{ p: 4, textAlign: 'center' }}>
            <Typography variant="h6" color="text.secondary">
              No content found
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
              {hasActiveFilters
                ? 'Try adjusting your filters'
                : !user
                ? 'Log in to see personalized content'
                : 'Check back later for new opportunities'}
            </Typography>
          </Paper>
        ) : (
          <Grid container spacing={3}>
            {getFilteredFeed().map((item) => {
              if (item.type === 'job') {
                const job = item.data as JobPosting;
                const employer = employers.get(job.employer_id);
                return (
                  <Grid item xs={12} key={`job-${job.id}`}>
                    <Card sx={{ '&:hover': { boxShadow: 6 }, transition: 'box-shadow 0.3s' }}>
                      <CardContent>
                        <Box sx={{ display: 'flex', alignItems: 'flex-start', mb: 2 }}>
                          <Avatar
                            src={employer?.company_picture_url}
                            sx={{ width: 56, height: 56, mr: 2, bgcolor: colors.frostedBlue }}
                          >
                            <BusinessIcon />
                          </Avatar>
                          <Box sx={{ flexGrow: 1 }}>
                            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 0.5 }}>
                              <Chip
                                icon={<WorkIcon />}
                                label="Job Posting"
                                size="small"
                                sx={{
                                  bgcolor: colors.brightTealBlue,
                                  color: 'white',
                                  fontWeight: 'bold',
                                }}
                              />
                            </Box>
                            <Typography variant="h6" sx={{ color: colors.deepTwilight, fontWeight: 'bold' }}>
                              {job.title}
                            </Typography>
                            <Typography variant="subtitle1" color="text.secondary">
                              {employer?.name || 'Unknown Company'}
                            </Typography>
                            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mt: 1 }}>
                              {job.location && (
                                <Chip
                                  icon={<LocationIcon />}
                                  label={job.location}
                                  size="small"
                                  variant="outlined"
                                />
                              )}
                              {job.experience_level && (
                                <Chip label={job.experience_level} size="small" variant="outlined" />
                              )}
                              {job.field && (
                                <Chip label={job.field} size="small" variant="outlined" />
                              )}
                            </Box>
                          </Box>
                          <Box sx={{ textAlign: 'right' }}>
                            <Typography variant="body2" color="text.secondary">
                              Posted {formatDate(job.posted_date)}
                            </Typography>
                          </Box>
                        </Box>

                        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                          {job.description.length > 300
                            ? `${job.description.substring(0, 300)}...`
                            : job.description}
                        </Typography>

                        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                          <Box sx={{ display: 'flex', alignItems: 'center' }}>
                            <MoneyIcon sx={{ mr: 0.5, color: colors.brightTealBlue }} />
                            <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                              {formatSalary(job.salary_min, job.salary_max)}
                            </Typography>
                          </Box>
                        </Box>
                      </CardContent>
                      <CardActions sx={{ justifyContent: 'flex-end', px: 2, pb: 2 }}>
                        <Button variant="outlined" size="small">
                          View Details
                        </Button>
                        <Button variant="contained" size="small" sx={{ bgcolor: colors.brightTealBlue }}>
                          Apply Now
                        </Button>
                      </CardActions>
                    </Card>
                  </Grid>
                );
              } else {
                const exp = item.data as UserExperience;
                const expUser = experienceUsers.get(exp.user_id);
                return (
                  <Grid item xs={12} key={`exp-${exp.id}`}>
                    <Card sx={{ '&:hover': { boxShadow: 6 }, transition: 'box-shadow 0.3s' }}>
                      <CardContent>
                        <Box sx={{ display: 'flex', alignItems: 'flex-start', mb: 2 }}>
                          <Avatar
                            src={expUser?.profile_picture_url}
                            sx={{ width: 48, height: 48, mr: 2, bgcolor: colors.turquoiseSurf }}
                          >
                            {expUser?.name?.charAt(0) || 'U'}
                          </Avatar>
                          <Box sx={{ flexGrow: 1 }}>
                            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 0.5 }}>
                              <Chip
                                icon={<PersonIcon />}
                                label="Experience"
                                size="small"
                                sx={{
                                  bgcolor: colors.turquoiseSurf,
                                  color: 'white',
                                  fontWeight: 'bold',
                                }}
                              />
                            </Box>
                            <Typography variant="h6" sx={{ color: colors.deepTwilight, fontWeight: 'bold' }}>
                              {exp.title}
                            </Typography>
                            <Typography variant="subtitle2" color="text.secondary">
                              {expUser?.name || 'Unknown User'}
                            </Typography>
                            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mt: 1 }}>
                              {exp.experience_type && (
                                <Chip label={exp.experience_type} size="small" variant="outlined" />
                              )}
                              {exp.is_current && (
                                <Chip label="Current" size="small" color="primary" variant="outlined" />
                              )}
                              {expUser?.location && (
                                <Chip
                                  icon={<LocationIcon />}
                                  label={expUser.location}
                                  size="small"
                                  variant="outlined"
                                />
                              )}
                            </Box>
                          </Box>
                          <Box sx={{ textAlign: 'right' }}>
                            <Typography variant="body2" color="text.secondary">
                              <CalendarIcon sx={{ fontSize: 14, verticalAlign: 'middle', mr: 0.5 }} />
                              {formatDate(exp.updated_at)}
                            </Typography>
                          </Box>
                        </Box>

                        <Typography variant="body2" color="text.secondary">
                          {exp.description}
                        </Typography>

                        {(exp.start_date || exp.end_date) && (
                          <Box sx={{ mt: 2 }}>
                            <Typography variant="caption" color="text.secondary">
                              {exp.start_date && `Started: ${formatMonthYear(exp.start_date)}`}
                              {exp.start_date && exp.end_date && ' • '}
                              {exp.end_date && `Ended: ${formatMonthYear(exp.end_date)}`}
                            </Typography>
                          </Box>
                        )}
                      </CardContent>
                    </Card>
                  </Grid>
                );
              }
            })}
          </Grid>
        )}
      </Box>
    </Container>
  );
};

export default Home;

