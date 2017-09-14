
var canvas = document.getElementById('gameCanvas');
var ctx = canvas.getContext("2d");
canvas.fillStyle = "black";

var circles = [];

//var circle = new Circle(30,30,29,"red");

//circle.xSpeed = 3;
//circle.ySpeed = 4;

var x = canvas.width;
var y = canvas.height;


/**
 * This function is for pure tests.
 * This is primarily to test the creation of a Circle.
 */
function textMovement()
{


    console.log(canvas.width);
    ctx.beginPath();
    ctx.arc(30, 30, 29, 0, 2 * Math.PI, false);
    ctx.fillStyle = "red";
    ctx.fill();
    ctx.stroke();
    ctx.closePath();
}

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
 * The Primary Loop of the Game.
 * Clears the background before every iteration.
 * Draws each circle accordingly
 * and performs a constant loop.
 */
function loop()
{
    ctx.clearRect(0,0,700,700);
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
    requestAnimationFrame(loop);
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
    loop();
}