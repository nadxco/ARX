const https = require('https');

const data = JSON.stringify({
  language: "python",
  version: "*",
  files: [{ name: "main.py", content: "print(1)" }]
});

const req = https.request({
  hostname: 'emacs.piston.rs',
  port: 443,
  path: '/api/v2/execute',
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Content-Length': data.length
  }
}, res => {
  let output = '';
  res.on('data', chunk => output += chunk);
  res.on('end', () => console.log(res.statusCode, output));
});

req.on('error', e => console.error(e));
req.write(data);
req.end();
