//https://afifalfiano.medium.com/angular-optimization-bundle-size-with-brotli-8222d753d6b2

const path = require('path');
const express = require('express');
const expressStaticGzip = require('express-static-gzip');

const serverPort = 7000;
const server = express();

server.use("/", expressStaticGzip(path.join(__dirname + '/dist/suits'), {
  enableBrotli: true
}));

server.get(/.*/, function (req, res, _next) {
  if (req.headers['x-forwarded-proto'] !== 'https' && process.env.NODE_ENV === 'production')
    res.redirect('https://' + req.hostname + req.url);
  else
    res.sendFile(__dirname + '/dist/suits/index.html');
});

server.listen(process.env.PORT || serverPort, function () {
  console.log('Server. http://localhost:%d', process.env.PORT || serverPort);
});
