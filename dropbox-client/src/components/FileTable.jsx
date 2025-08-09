import React from 'react';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Button from '@mui/material/Button';
import DownloadIcon from '@mui/icons-material/Download';
import VisibilityIcon from '@mui/icons-material/Visibility';
import { getViewUrl } from '../api/fileApi';
import '../App.css';

const FileTable = ({ files, onDownload }) => {
  const handleView = (id) => {
    const url = getViewUrl(id);
    window.open(url, '_blank', 'noopener,noreferrer');
  };

  return (
    <TableContainer component={Paper} className="centered-table">
      <Table stickyHeader>
        <TableHead>
          <TableRow>
            <TableCell>File Name</TableCell>
            <TableCell>File Size (bytes)</TableCell>
            <TableCell>Upload Date</TableCell>
            <TableCell>Action</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {files.map((file) => (
            <TableRow key={file.id}>
              <TableCell>{file.fileName}</TableCell>
              <TableCell>{file.fileSize}</TableCell>
              <TableCell>{new Date(file.uploadedAt).toLocaleString()}</TableCell>
              <TableCell>
                <div className="action-buttons">
                  <Button
                    variant="contained"
                    color="primary"
                    startIcon={<DownloadIcon />}
                    onClick={() => onDownload(file.id, file.fileName)}
                    className="action-btn"
                  >
                    Download
                  </Button>
                  <Button
                    variant="outlined"
                    color="secondary"
                    startIcon={<VisibilityIcon />}
                    onClick={() => handleView(file.id)}
                    className="action-btn"
                  >
                    View
                  </Button>
                </div>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

export default FileTable;
