const http = require('http');

const server = http.createServer((req, res) => {
    if (req.method === 'POST' ){  //&& req.url === '/calculate') {
        handlePost(req, res);
    } 
    else {
        res.writeHead(405, { 'Content-Type': 'text/plain' });
        res.end('Method Not Allowed');
    }
});

function handlePost(req, res) {
    let number = (req.url.split("?")[1])
    number = number.split("=")[1]
    number = parseInt(number)
    console.log(`number = ${number}`)
    
    req.on('data', (chunk) => {
        console.log('POST data chunk: ' + chunk);
    });

    req.on('end', () => {
        console.log('POST request ended');
        if (!isNaN(number)) {
            const result = calculateResult(number);
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            let resultString = JSON.stringify(result)
            res.end(resultString);
            console.log(resultString);

        } else {
            res.writeHead(400, { 'Content-Type': 'text/plain' });
            res.end('Bad Request');
        }
    });
}

function calculateResult(x) {
    return x + 1;
    
}

const PORT = 8042;
server.listen(PORT, () => {
    console.log(`Server running at http://localhost:${PORT}`);
});