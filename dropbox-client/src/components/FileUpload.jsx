import React, { useRef, useState } from 'react';
import Button from '@mui/material/Button';
import Box from '@mui/material/Box';
import Typography from '@mui/material/Typography';
import '../App.css';

const allowedTypes = [
  'image/png',
  'image/jpg',
  'text/plain',
  'application/json',
  'application/pdf'
];

const FileUpload = ({ onUpload }) => {
  const fileInputRef = useRef();
  const [selectedFile, setSelectedFile] = useState(null);
  const [error, setError] = useState('');

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file && allowedTypes.includes(file.type)) {
      setSelectedFile(file);
      setError('');
    } else {
      setSelectedFile(null);
      setError('Invalid file type. Allowed: png, jpg, json, txt and pdf only');
    }
  };

  const handleUpload = () => {
    if (selectedFile) {
      onUpload(selectedFile);
      setSelectedFile(null);
      fileInputRef.current.value = '';
    }
  };

  return (
    <Box className="upload-row">
      <input
        type="file"
        ref={fileInputRef}
        style={{ display: 'none' }}
        onChange={handleFileChange}
        accept=".png,.jpg,.txt,.pdf,.json"
      />
      <Button variant="contained" component="span" onClick={() => fileInputRef.current.click()}>
        Choose File
      </Button>
      <Typography variant="body2">
        {selectedFile ? selectedFile.name : 'No file selected'}
      </Typography>
      <Button
        variant="contained"
        color="success"
        disabled={!selectedFile}
        onClick={handleUpload}
      >
        Upload
      </Button>
      {error && <Typography color="error">{error}</Typography>}
    </Box>
  );
};

export default FileUpload;
