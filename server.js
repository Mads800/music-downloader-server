const express = require('express');
const path = require('path');
const fs = require('fs');

const app = express();

// مجلد الملفات
const FILES_DIR = path.join(__dirname, 'uploads');

// Middleware لتقديم الملفات بشكل ثابت
app.use('/files', express.static(FILES_DIR));

// عرض ملف محدد
app.get('/files/:filename', (req, res) => {
  const filePath = path.join(FILES_DIR, req.params.filename);
  if (fs.existsSync(filePath)) {
    res.sendFile(filePath);
  } else {
    res.status(404).json({ error: 'File not found' });
  }
});

// قائمة جميع الملفات (مثلاً للعرض في التطبيق)
app.get('/list', (req, res) => {
  fs.readdir(FILES_DIR, (err, files) => {
    if (err) return res.status(500).json({ error: 'Cannot read files' });
    const list = files.map(f => ({
      name: f,
      url: `/files/${f}`
    }));
    res.json(list);
  });
});

// دعم البورت الديناميكي من Render أو localhost
const PORT = process.env.PORT || 3000;

app.listen(PORT, () => {
  console.log(`✅ Server running on port ${PORT}`);
});
