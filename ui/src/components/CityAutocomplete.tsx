import React, { useState, useEffect } from 'react';
import { Autocomplete, TextField, CircularProgress } from '@mui/material';
import { debounce } from '@mui/material/utils';

interface City {
  id: number;
  name: string;
  asciiName: string;
  countryCode: string;
  admin1Code?: string;
  population?: number;
  latitude?: number;
  longitude?: number;
}

interface CityAutocompleteProps {
  value: string;
  onChange: (value: string) => void;
  label?: string;
  placeholder?: string;
  required?: boolean;
  error?: boolean;
  helperText?: string;
  fullWidth?: boolean;
}

const CityAutocomplete: React.FC<CityAutocompleteProps> = ({
  value,
  onChange,
  label = 'Location',
  placeholder = 'Search for a city...',
  required = false,
  error = false,
  helperText,
  fullWidth = true,
}) => {
  const [inputValue, setInputValue] = useState(value || '');
  const [options, setOptions] = useState<City[]>([]);
  const [loading, setLoading] = useState(false);
  const [selectedCity, setSelectedCity] = useState<City | null>(null);

  // Fetch cities from API
  const fetchCities = async (searchTerm: string) => {
    if (!searchTerm || searchTerm.length < 2) {
      setOptions([]);
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(
        `http://localhost:8080/api/cities/search?name=${encodeURIComponent(searchTerm)}`
      );
      const result = await response.json();

      if (result.success && Array.isArray(result.data)) {
        setOptions(result.data);
      } else {
        setOptions([]);
      }
    } catch (error) {
      console.error('Error fetching cities:', error);
      setOptions([]);
    } finally {
      setLoading(false);
    }
  };

  // Debounced search function
  const debouncedFetchCities = React.useMemo(
    () => debounce(fetchCities, 300),
    []
  );

  // Handle input change
  useEffect(() => {
    if (inputValue) {
      debouncedFetchCities(inputValue);
    } else {
      setOptions([]);
    }
  }, [inputValue, debouncedFetchCities]);

  // Format city display string
  const formatCityLabel = (city: City): string => {
    const parts = [city.name];
    
    if (city.admin1Code) {
      parts.push(city.admin1Code);
    }
    
    parts.push(city.countryCode);
    
    if (city.population) {
      parts.push(`Pop: ${city.population.toLocaleString()}`);
    }
    
    return parts.join(', ');
  };

  // Format city value for storage (simpler format)
  const formatCityValue = (city: City): string => {
    const parts = [city.name];
    
    if (city.admin1Code) {
      parts.push(city.admin1Code);
    }
    
    parts.push(city.countryCode);
    
    return parts.join(', ');
  };

  return (
    <Autocomplete
      fullWidth={fullWidth}
      value={selectedCity}
      onChange={(_event, newValue) => {
        setSelectedCity(newValue);
        if (newValue) {
          const formattedValue = formatCityValue(newValue);
          onChange(formattedValue);
          setInputValue(formattedValue);
        } else {
          onChange('');
          setInputValue('');
        }
      }}
      inputValue={inputValue}
      onInputChange={(_event, newInputValue, reason) => {
        // Only update input value if user is typing (not selecting)
        if (reason === 'input') {
          setInputValue(newInputValue);
        } else if (reason === 'clear') {
          setInputValue('');
          setSelectedCity(null);
          onChange('');
        }
      }}
      options={options}
      getOptionLabel={(option) => formatCityLabel(option)}
      isOptionEqualToValue={(option, value) => option.id === value.id}
      loading={loading}
      filterOptions={(x) => x} // Disable client-side filtering since we're doing server-side search
      noOptionsText={
        inputValue.length < 2
          ? 'Type at least 2 characters to search'
          : loading
          ? 'Searching...'
          : 'No cities found'
      }
      renderInput={(params) => (
        <TextField
          {...params}
          label={label}
          placeholder={placeholder}
          required={required}
          error={error}
          helperText={helperText}
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <>
                {loading ? <CircularProgress color="inherit" size={20} /> : null}
                {params.InputProps.endAdornment}
              </>
            ),
          }}
        />
      )}
      renderOption={(props, option) => (
        <li {...props} key={option.id}>
          <div>
            <div style={{ fontWeight: 500 }}>
              {option.name}, {option.countryCode}
            </div>
            {(option.admin1Code || option.population) && (
              <div style={{ fontSize: '0.875rem', color: '#666' }}>
                {option.admin1Code && <span>{option.admin1Code}</span>}
                {option.admin1Code && option.population && <span> • </span>}
                {option.population && (
                  <span>Population: {option.population.toLocaleString()}</span>
                )}
              </div>
            )}
          </div>
        </li>
      )}
    />
  );
};

export default CityAutocomplete;

