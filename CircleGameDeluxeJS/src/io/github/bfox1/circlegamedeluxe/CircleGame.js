
/* Initializing Canvas and CTX */
var canvas = document.getElementById('gameCanvas');
var ctx = canvas.getContext("2d");
canvas.fillStyle = "black";

/*Setting up Menu*/

setMenu();

/* Registering Click Listeners */

registerClickListeners();

/*Variable inits */

var points = 0;
var interval = 5;
var textPoint = new DisplayText("Points: ",80, 40);
var textPointI = new DisplayText(points, 70, 75);
var textTimeLeft = new DisplayText("Time Left:", 350, 40);
var textInterval = new DisplayText(interval, 345, 75);

var myVar;

var totalInterval = 60;

var circles = [];

var circleIndex = [];

var x = canvas.width;
var y = canvas.height;

var gameState = 0;

var endFlag = false;

/**
 * This is a function that Creates a Circle Object. Rather
 * Complicated, it can be made easier to read.
 * @param x The starting location of the Circle on the X axis
 * @param y The Starting location of the Circle on the Y axis
 * @param rad The radius of the Circle.
 * @param color The Color of the Circle.
 * @constructor inits the circlef
 */
function Circle(x, y, rad, color)
{
    var _this = this;

    (function()
    {
        _this.x = x || null;
        _this.y = y || null;
        _this.radius = rad|| null;
        _this.color = color || null;
    })();

    /**
     * To simply put it, this function draws the circle to the canvas.
     * This function gets invoked within the loop function.
     * @param ctx the main context of the GameCanvas
     */
    this.draw = function (ctx)
    {
        if(!_this.x || !_this.y || !_this.radius || !_this.color)
        {
            console.error("Circle Requires an x, y, radius and color.");
            return;
        }
        ctx.beginPath();
        ctx.arc(_this.x, _this.y, _this.radius,  0, 2*Math.PI, false);
        ctx.fillStyle = _this.color;
        ctx.fill();
        ctx.closePath();

    }
}

/**
 * Simple creates a TextObject for special text on the Canvas.
 * @param name
 * @param x
 * @param y
 * @constructor
 */
function DisplayText(name, x, y)
{
    var _this = this;

    (function()
    {
        _this.name = name || null;
        _this.x = x || null;
        _this.y = y || null;
    })();

    this.draw = function(ctx)
    {

        ctx.font = "30px Comic Sans MS";
        ctx.fillStyle = "white";
        ctx.textAlign = "center";
        ctx.fillText(_this.name, _this.x, _this.y);

    }
}

/**
 * Main game Loop for the Circle Game.
 * Processes every Gamestate it comes across.
 * GameState 0 = Game Menu
 * GameState 1 = Active State
 * GameState 2 = PauseMenu
 * GameState 3 =
 */
function loop()
{

    if(gameState === 1)
    {
        ctx.clearRect(0,0,700,700);

        gameUpdate();
    }
    else if(gameState === 2)
    {
        pauseMenu();
        var song = document.getElementById("songList");
        song.pause();

    }
    else if(gameState === 3)
    {
        ctx.clearRect(0,0,700,700);
        displayGameEndMenu();
    }

    if(!endFlag)
    {
        requestAnimationFrame(loop);
    }
}

/**
 * Function used to check Wall Collisions.
 * @param circle
 */
function moveCircle(circle)
{

    var posX = circle.x;
    var posY = circle.y;
    var xSpeed = circle.xSpeed;
    var ySpeed = circle.ySpeed;
    checkWallCollision();

    /**
     * inner function to segregate functionality and to clean up code.
     */
    function checkWallCollision()
    {
        if(posX < circle.radius)
        {
            posX = circle.radius;
            circle.xSpeed = Math.abs(xSpeed);
        }
        else if(posX > x - circle.radius)
        {
            posX = x - circle.radius;
            circle.xSpeed = -Math.abs(xSpeed)
        }
        if(posY < circle.radius)
        {
            posY = circle.radius;
            circle.ySpeed = Math.abs(ySpeed);
        }
        else if(posY > y - circle.radius)
        {
            posY = y - circle.radius;
            circle.ySpeed = -Math.abs(ySpeed);
        }
        posX += xSpeed;
        posY += ySpeed;

        circle.x = posX;
        circle.y = posY;
    }

}

/**
 * Game Start Function. everything gets started here.
 */
function gameStart()
{
    for(var i = 0; i < 10; i++)
    {
        generateCircles();
    }
    startTimer();
    loop();
}

function startTimer()
{
    myVar = setInterval(function()
    {
    interval--;
    totalInterval++;
    }, 1000);
}



/**
 * This is the SubLoop for the Game.
 * This Loop focuses on updating the Circles.
 */
function gameUpdate()
{

    if(interval === 0)
    {
        clearInterval(myVar);
        gameState = 3;
    }
    if(circleIndex.length > 0) {

        for (var x = 0; x < circleIndex.length; x++)
        {
            if(circles[circleIndex[x]].color === "blue")
            {
                interval += 10;
            }
            circles.splice(circleIndex[x], 1);
            circles.push(generateRandomCircle());

        }

        circleIndex.length =0;
    }

    /**
     * First Loop checks for collisions against walls.
     */
    for(var i = 0; i < circles.length; i++)
    {
        moveCircle(circles[i]);
        circles[i].draw(ctx);
    }
    /**
     * Second Looop checks for Collisions against other circles.
     */
    for(var f = 0; f < circles.length; f++)
    {

        checkCircleCollision(circles[f]);
        //circles[f].draw(ctx);
    }

    writeToCanvas();
}

