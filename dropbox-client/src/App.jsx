import React, { useEffect, useState } from "react";
import Container from "@mui/material/Container";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Snackbar from "@mui/material/Snackbar";
import MuiAlert from "@mui/material/Alert";
import FileTable from "./components/FileTable";
import FileUpload from "./components/FileUpload";
import { getFiles, uploadFile, downloadFile } from "./api/fileApi";

const Alert = React.forwardRef(function Alert(props, ref) {
  return <MuiAlert elevation={6} ref={ref} variant="filled" {...props} />;
});

const App = () => {
  const [files, setFiles] = useState([]);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success",
  });
  const [loading, setLoading] = useState(false);

  const fetchFiles = async () => {
    setLoading(true);
    try {
      const data = await getFiles();
      setFiles(data);
    } catch (err) {
      setSnackbar({
        open: true,
        message: "Failed to fetch files",
        severity: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFiles();
  }, []);

  const handleUpload = async (file) => {
    try {
      setLoading(true);
      const res = await uploadFile(file);
      setSnackbar({ open: true, message: res.message, severity: "success" });
      fetchFiles();
    } catch (err) {
      setSnackbar({ open: true, message: "Upload failed", severity: "error" });
    } finally {
      setLoading(false);
    }
  };

  const handleDownload = async (id, fileName) => {
    try {
      const response = await downloadFile(id);
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", fileName);
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
    } catch (err) {
      setSnackbar({
        open: true,
        message: "Download failed",
        severity: "error",
      });
    }
  };

  return (
    <Container maxWidth="lg" sx={{ mt: 4 }}>
      <Typography variant="h4" align="center" gutterBottom>
        Simple Dropbox
      </Typography>
      <FileUpload onUpload={handleUpload} />
      <Box mt={4}>
        {files && files.length > 0 ? (
          <FileTable files={files} onDownload={handleDownload} />
        ) : (
          <Typography variant="h5" align="left" gutterBottom>
            No files found. Please upload some files to view the list.
          </Typography>
        )}
      </Box>
      <Snackbar
        open={snackbar.open}
        autoHideDuration={3000}
        onClose={() => setSnackbar({ ...snackbar, open: false })}
      >
        <Alert severity={snackbar.severity} sx={{ width: "100%" }}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Container>
  );
};

export default App;
