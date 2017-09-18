/**
 * Very useful utility to generate a random number
 * @param min minimum number to generate
 * @param max maximum number to generate
 * @returns {Number}
 */
function  randomInt(min, max)
{
    var rand = (Math.random() * max) + 1;

    var total = rand + min;

    if(total > max) total -= total - max;
    if(total < min) total += min - total;

    return parseInt(total);
}

/**
 * Randomly Generates Circles and pushes them to the circles[] List.
 */
function generateCircles()
{
    var blue = randomInt(0, 15);
    var circle;
    if(blue === 10)
    {
        circle = new Circle(randomInt(30, x-29), randomInt(30, y-29), randomInt(15, 30), "blue");
        circle.xSpeed = randomInt(3, 7);
        circle.ySpeed = randomInt(3, 7);
        circles.push(circle)
    }
    else
    {
        circle = new Circle(randomInt(30, x-29), randomInt(30, y-29), randomInt(15, 30), "red");
        circle.xSpeed = randomInt(3, 7);
        circle.ySpeed = randomInt(3, 7);
        circles.push(circle);
    }
}


function generateRandomCircle()
{
    var blue = randomInt(0, 15);
    var circle;


        if(blue === 10)
        {
            circle = new Circle(randomInt(30, x-29), randomInt(30, y-29), randomInt(15, 30), "blue");
            circle.xSpeed = randomInt(3, 7);
            circle.ySpeed = randomInt(3, 7);
        }
        else
        {
            circle = new Circle(randomInt(30, x-29), randomInt(30, y-29), randomInt(15, 30), "red");
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

function checkIntersects(circle, x1, y1)
{
    var x = circle.x - x1;
    var y = circle.y - y1;
    var rSum = circle.radius + 1;

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
            bloop.load();
            bloop.play();
            return;
        }
    }
}

function registerClickListeners()
{

    var oFlag = false;
    canvas.addEventListener('click', function(evt)
    {
        var x = evt.pageX - canvas.offsetLeft;
        var y = evt.pageY - canvas.offsetTop;
        if(gameState === 0)
        {


            var flag;

            console.log(x + " : " + y);
            var sMinX = 225;
            var sMaxX = 485;
            var sMinY = 290;
            var sMaxY = 409;

            if (sMinX <= x && sMaxX >= x)
            {
                if (sMinY <= y && sMaxY >= y)
                {
                    var audioCtx = new (window.AudioContext || window.webkitAudioContext)();
                    console.log("Im clicked");
                    flag = true;
                    gameStart();
                    var song = document.getElementById("songList");
                    //var source = audioCtx.createMediaElementSource(song);
                    //var gainNode = audioCtx.createGain();
                    //gainNode.gain.value = 0.2;
                    //source.connect(gainNode);
                    //gainNode.connect(audioCtx.destination);
                    song.autoplay = true;
                    song.load();

                    gameState = 1;
                }
            }
        }
        else if(gameState === 2)
        {
            var rMinX = 225;
            var rMaxX = 485;
            var rMinY = 290;
            var rMaxY = 409;

            var oMinX = 225;
            var oMinY = 420;
            var oMaxX = 485;
            var oMaxY = 538;

            if (rMinX <= x && rMaxX >= x)
            {
                if (rMinY <= y && rMaxY >= y)
                {
                    gameState = 1;
                    var song = document.getElementById("songList");
                    song.play();
                    song.style.display = "none"
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

            for (var i = 0; i < circles.length; i++)
            {
                 if (checkIntersects(circles[i], x, y + 5))
                 {
                    circleIndex.push(i);
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
                }
            }
            else if(gameState === 2)
            {
                if(e.keyCode === 101)
                {
                    gameState = 1;
                    var song = document.getElementById("songList");
                    song.play();
                    song.style.display = "none"

                }
            }

        });

}


function setMenu()
{
    ctx.drawImage(document.getElementById("Menu"), 1,1,698,698)
}

function pauseMenu()
{
    ctx.drawImage(document.getElementById("PauseMenu"),1,1, 698, 698);
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


function updatePoints()
{
    var pointText = document.getElementById("pointText");

    pointText.innerHTML = "Points: " + points;
}

function updateInterval()
{
    var intervalText = document.getElementById("interval");

    intervalText.innerHTML = "Time Left: " + interval;
}