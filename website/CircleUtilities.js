/**
 * Very useful utility to generate a random number
 * @param min minimum number to generate
 * @param max maximum number to generate
 * @returns {Number}
 */
function randomInt(min, max)
{
    var rand = (Math.random() * max) + 1;

    var total = rand + min;

    if(total > max) total -= total - max;
    if(total < min) total += min - total;

    return parseInt(total);
}

function calculateScore()
{
    return Math.round((points / misses)*totalInterval);
}

function displayFinalResults()
{
    var s = calculateScore();
    console.log(points);
    var finalPoints = new DisplayText(points === 0 ? "0" : points, 79,60, "white");
    var finalMisses = new DisplayText(misses === 0 ? "0" : misses, 345,60,"white");
    var finalTime = new DisplayText(totalInterval, 585,60,"white");
    var finalScore = new DisplayText(s === null || s === "null" ? "0" : s, 350,216, "white");

    finalPoints.draw(ctx);
    finalMisses.draw(ctx);
    finalTime.draw(ctx);
    finalScore.draw(ctx);

    var prevScore = localStorage.getItem("score");

    if(prevScore !== null && prevScore !== "null")
    {
        if(prevScore < score)
        {
            localStorage.setItem("score", score);
        }
    }
     score = localStorage.getItem("score");

}

/**
 * Randomly Generates Circles and pushes them to the circles[] List.
 */
function generateCircles()
{
    var blue = randomInt(0, 13);
    var circle;
    if(blue === 10)
    {
        circle = new Circle3d(randomInt(30, x-29), randomInt(30, y-29), randomInt(15, 30), "blue");
        circle.xSpeed = randomInt(3, 7);
        circle.ySpeed = randomInt(3, 7);
        circles.push(circle)
    }
    else
    {
        circle = new Circle3d(randomInt(30, x-29), randomInt(30, y-29), randomInt(15, 30), "red");
        circle.xSpeed = randomInt(3, 7);
        circle.ySpeed = randomInt(3, 7);
        circles.push(circle);
    }
}

/**
 * Generates individual Circles and returns a Circle.
 * @returns {*}
 */
function generateRandomCircle()
{
    var blue = randomInt(0, 13);
    var circle;


        if(blue === 10)
        {
            circle = new Circle3d(randomInt(30, x-29), randomInt(30, y-29), randomInt(15, 30), "blue");
            circle.xSpeed = randomInt(3, 7);
            circle.ySpeed = randomInt(3, 7);

        }
        else
        {
            circle = new Circle3d(randomInt(30, x-29), randomInt(30, y-29), randomInt(15, 30), "red");
            circle.xSpeed = randomInt(3, 7);
            circle.ySpeed = randomInt(3, 7);
        }


    return circle;
}

/**
 * Determines if 2 circles intersect, if they do, it returns true.
 * @param circle direct Circle in scope.
 * @param circleB The indirect Circle within List being tested against.
 * @returns {boolean}
 */
function ifIntersects(circle, circleB)
{
    var x = circle.x - circleB.x;
    var y = circle.y - circleB.y;
    var rSum = circle.radius + circleB.radius;

    var bool = (x * x + y * y <= rSum*rSum);

    return bool

}

/**
 * Checking if Circle intersects with click points.
 * @param circle
 * @param x1
 * @param y1
 * @returns {boolean}
 */
function checkIntersects(circle, x1, y1)
{
    var x = circle.x - x1;
    var y = circle.y - y1;
    var rSum = circle.radius + 5;

    return (x*x+y*y <= rSum*rSum)
}

/**
 * Checks Collisions against Circles
 * @param circle
 */
function checkCircleCollision(circle)
{
    for(var i = 0; i < circles.length; i++)
    {
        if(circle !== circles[i] && ifIntersects(circle, circles[i]))
        {

            var tempX = circle.xSpeed;
            var tempY = circle.ySpeed;
            circle.xSpeed = circles[i].xSpeed;
            circle.ySpeed = circles[i].ySpeed;
            circles[i].xSpeed = tempX;
            circles[i].ySpeed = tempY;

            circles[i].x += tempX;
            circles[i].y += tempY;
            circle.x += circle.xSpeed;
            circle.y += circle.ySpeed;
            var bloop = document.getElementById("boop");
            var bloop2 = document.getElementById("boop2");
            console.log(bloop.duration + " : " + bloop.currentTime);
            if(bloop.duration > 0 )
            {

                bloop.load();
                bloop.play();
            }
            else if(bloop2.duration > 0 )
            {
                bloop.load();

                bloop2.play();
            }
            return;
        }
    }
}

/**
 * Writes Text to Canvas.
 */
function writeToCanvas()
{
    textPointI.name = points;
    textInterval.name = interval;
    textPoint.draw(ctx);
    textTimeLeft.draw(ctx);
    textInterval.draw(ctx);
    textPointI.draw(ctx)
}

/**
 * Registering Click Listeners and all the good stuff that
 * goes on in the Game.
 */
function registerClickListeners()
{

    var oFlag = false;
    canvas.addEventListener('click', function(evt)
    {
        var x = evt.pageX - canvas.offsetLeft;
        var y = evt.pageY - canvas.offsetTop;

        var rMinX = 225;
        var rMaxX = 485;
        var rMinY = 290;
        var rMaxY = 409;

        var oMinX = 225;
        var oMinY = 420;
        var oMaxX = 485;
        var oMaxY = 538;

        console.log(x + " : " + y);
        //GameState equals Main Menu State.
        if(gameState === 0)
        {


            var flag;




            if (rMinX <= x && rMaxX >= x)
            {
                if (rMinY <= y && rMaxY >= y)
                {
                    var audioCtx = new (window.AudioContext || window.webkitAudioContext)();
                    console.log("Im clicked");
                    flag = true;
                    var song = document.getElementById("songList");
                    //var source = audioCtx.createMediaElementSource(song);
                    //var gainNode = audioCtx.createGain();
                    //gainNode.gain.value = 0.2;
                    //source.connect(gainNode);
                    //gainNode.connect(audioCtx.destination);
                    song.autoplay = true;
                    song.load();

                    gameState = 1;
                    gameStart();

                }
            }
            if (oMinX <= x && oMaxX >= x)
            {
                if (oMinY <= y && oMaxY >= y)
                {
                    if(oFlag)
                    {
                        closeOptionSubMenu();
                        oFlag = false;
                    }
                    else
                    {
                        openOptionMenu();
                        oFlag = true;
                    }
                }
            }
        }
        else if(gameState === 2)
        {

            if (rMinX <= x && rMaxX >= x)
            {
                if (rMinY <= y && rMaxY >= y)
                {
                    gameState = 1;
                    var song = document.getElementById("songList");
                    song.play();
                    song.style.display = "none";
                    startTimer();
                    return;
                }
            }
            if (oMinX <= x && oMaxX >= x)
            {
                if (oMinY <= y && oMaxY >= y)
                {
                    if(oFlag)
                    {
                        closeOptionSubMenu();
                        oFlag = false;
                    }
                    else
                    {
                        openOptionMenu();
                        oFlag = true;
                    }
                }
            }

        }
        else if(gameState === 3)
        {
            if (rMinX <= x && rMaxX >= x)
            {
                if (rMinY <= y && rMaxY >= y)
                {
                    gameState = 0;


                    ctx.clearRect(0,0, 700,700);

                    restartGame();
                    setMenu();
                    endFlag = false;
                    return;
                }
            }
            if (oMinX <= x && oMaxX >= x)
            {
                if (oMinY <= y && oMaxY >= y)
                {
                    if(oFlag)
                    {
                        closeOptionSubMenu();
                        oFlag = false;
                    }
                    else
                    {
                        openOptionMenu();
                        oFlag = true;
                    }
                }
            }
        }

    });

    canvas.onmousedown = function(event)
    {
        if (gameState === 1)
        {

            var x = event.clientX - canvas.offsetLeft;
            var y = event.clientY - canvas.offsetTop;
            misses++;

            for (var i = 0; i < circles.length; i++)
            {
                 if (checkIntersects(circles[i], x, y + 5))
                 {
                    circleIndex.push(i);
                    points++;
                    break;
                }
            }
        }
    };

    window.addEventListener("keypress", function(e)
        {
            if(gameState === 1) {
                if (e.keyCode === 101) {
                    gameState = 2;
                    clearInterval(myVar);
                }
            }
            else if(gameState === 2)
            {
                if(e.keyCode === 101)
                {
                    gameState = 1;
                    var song = document.getElementById("songList");
                    song.play();
                    song.style.display = "none";
                    startTimer();

                }
            }

        });

}


function setMenu()
{
    ctx.drawImage(document.getElementById("Menu"), 1,1,698,698);
    var scoreText = new DisplayText("HighScore", 350,30, "black");
    var s = new DisplayText(score === null || score === "null" ? "0" : score, 350, 60, "black");
    scoreText.draw(ctx);
    s.draw(ctx);
}

function pauseMenu()
{
    ctx.drawImage(document.getElementById("PauseMenu"),1,1, 698, 698);
}

function displayGameEndMenu()
{
    ctx.drawImage(document.getElementById("EndMenu"),1,1,698, 698);
    endFlag = true;
}

function openOptionMenu()
{
    var song = document.getElementById("songList");

    song.style.display = "block";
}

function closeOptionSubMenu()
{
    var song = document.getElementById("songList");

    song.style.display = "none";
}

function restartGame()
{
    var song = document.getElementById("songList")
    song.pause();
    song.currentTime = 0;
    song.style.display = "none";

    points = 0;
    interval = 60;
    misses = 0;
    totalInterval = 0;
    circles.length = 0;
}

function sendDataToServer()
{
    var ws = new WebSocket("ws://192.168.1.84:8080/");

    if(ws.readyState !== WebSocket.CLOSED)
    {
        ws.onopen = function()
        {
            console.log("Sending Data");
            ws.send("bfox");
            ws.send(score);
        }
    }
}

