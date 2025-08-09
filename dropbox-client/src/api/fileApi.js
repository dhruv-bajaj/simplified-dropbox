import axios from 'axios';

const API_BASE_URL = 'http://localhost:8081/dropbox';

export const getFiles = async () => {
  const response = await axios.get(`${API_BASE_URL}/listAllFiles`);
  return response.data;
};

export const uploadFile = async (file) => {
  const formData = new FormData();
  formData.append('file', file);
  const response = await axios.post(`${API_BASE_URL}/uploadFile`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  });
  return response.data;
};

export const downloadFile = async (id) => {
  const response = await axios.get(`${API_BASE_URL}/download/${id}`, {
    responseType: 'blob',
  });
  return response;
};

export const getViewUrl = (id) => `${API_BASE_URL}/view/${id}`;