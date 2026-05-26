const https = require('https');
https.get('https://emacs.piston.rs/api/v2/runtimes', (res) => {
    let data = '';
    res.on('data', chunk => data += chunk);
    res.on('end', () => console.log(res.statusCode, data.substring(0, 100)));
}).on('error', err => console.log('Error:', err.message));
