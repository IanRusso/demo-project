import React from 'react';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import dayjs, { Dayjs } from 'dayjs';

interface MonthYearPickerProps {
  label: string;
  value: string;
  onChange: (value: string) => void;
  disabled?: boolean;
  required?: boolean;
  fullWidth?: boolean;
  error?: boolean;
  helperText?: string;
}

const MonthYearPicker: React.FC<MonthYearPickerProps> = ({
  label,
  value,
  onChange,
  disabled = false,
  required = false,
  fullWidth = true,
  error = false,
  helperText,
}) => {
  const dayjsValue = value ? dayjs(value, 'YYYY-MM') : null;

  const handleChange = (newValue: Dayjs | null) => {
    if (newValue && newValue.isValid()) {
      onChange(newValue.format('YYYY-MM'));
    } else {
      onChange('');
    }
  };

  return (
    <DatePicker
      label={label}
      value={dayjsValue}
      onChange={handleChange}
      disabled={disabled}
      views={['year', 'month']}
      openTo="year"
      format="MMM YYYY"
      slotProps={{
        textField: {
          fullWidth,
          required,
          error,
          helperText,
        },
      }}
    />
  );
};

export default MonthYearPicker;

