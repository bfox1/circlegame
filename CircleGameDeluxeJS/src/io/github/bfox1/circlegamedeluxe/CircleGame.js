
var container = document.getElementById("myContainer");

var x = container.offsetWidth;
var y = container.offsetHeight;

function moveSquare()
{
    var elem = document.getElementById("myAnimation");
    var posX = 0;
    var posY = 0;
    var xSpeed = 3;
    var ySpeed = 5;
    var id = setInterval(frame, 10);

    function frame()
    {


        if(posX < 0)
        {
            posX = 0;
            xSpeed = Math.abs(xSpeed);
        }
        else if(posX > x - 50)
        {
            posX = x - 50;
            xSpeed = -Math.abs(xSpeed)
        }
        if(posY < 0)
        {
            posY = 0;
            ySpeed = Math.abs(ySpeed);
        }
        else if(posY > y - 50)
        {
            posY = y - 50;
            ySpeed = -Math.abs(ySpeed);
        }
        posX += xSpeed;
        posY += ySpeed;

        elem.style.top = posX + 'px';
        elem.style.left = posY + 'px';

    }
}