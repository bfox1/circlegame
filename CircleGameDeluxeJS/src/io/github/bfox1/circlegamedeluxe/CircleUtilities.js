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
        circle = new Circle(randomInt(30, x-29), randomInt(30, y-29), randomInt(10, 30), "blue");
        circle.xSpeed = randomInt(3, 7);
        circle.ySpeed = randomInt(3, 7);
        circles.push(circle)
    }
    else
    {
        circle = new Circle(randomInt(30, x-29), randomInt(30, y-29), randomInt(10, 30), "red");
        circle.xSpeed = randomInt(3, 7);
        circle.ySpeed = randomInt(3, 7);
        circles.push(circle);
    }
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
            return;
        }
    }
}