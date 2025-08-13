const ytdl = require('ytdl-core');
const fs = require('fs');

async function downloadAudio(videoId, outputPath) {
  const url = `https://www.youtube.com/watch?v=${videoId}`;
  return new Promise((resolve, reject) => {
    ytdl(url, { filter: 'audioonly' })
      .pipe(fs.createWriteStream(outputPath))
      .on('finish', () => {
        console.log(`Downloaded audio to ${outputPath}`);
        resolve();
      })
      .on('error', reject);
  });
}

// مثال استخدام:
// downloadAudio('S9bCLPwzSC0', './eminem.mp3');
