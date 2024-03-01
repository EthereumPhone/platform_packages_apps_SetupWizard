var deviceWidth = 421; //881,421,412
var deviceHeight = 861; //881,861,846
var lineWidth;
var maxLogoAmount = 900;
var attempts = 100;
var logos = [];
var step = 20;
var xoff = 0;
var minSize = 20;
var maxSize = 200;

var colorpalletes = [

    ["#0081A7", "#00AFB9", "#F07167", "#FFFD98", "#BDE4A7"],
    ["#FF595E", "#FFCA3A", "#8AC926", "#1982C4", "#6A4C93"],
    ["#4D5057", "#4E6E5D", "#4DA167", "#3BC14A", "#CFCFCF"],
    ["#2C6E49", "#4C956C", "#823329", "#FFC9B9", "#D68C45"],
    ["#264653", "#2A9D8F", "#E9C46A", "#F4A261", "#E76F51"],
    ["#003049", "#D62828", "#F77F00", "#FCBF49", "#D0E37F"],
    ["#485696", "#E7E7E7", "#F9C784", "#FC7A1E", "#F24C00"],
    ["#F24C00", "#412234", "#6D466B", "#B49FCC", "#EAD7D7"],
    ["#2F323A", "#77567A", "#C47AC0", "#E39EC1", "#DEBAC0"],
    ["#ECA400", "#EAF8BF", "#006992", "#27476E", "#001D4A"],
    ["#231F20", "#BB4430", "#7EBDC2", "#F3DFA2", "#EFE6DD"],
    ["#FFC15E", "#F7B05B", "#F7934C", "#CC5803", "#1F1300"],
    ["#E2CFEA", "#A06CD5", "#6247AA", "#102B3F", "#062726"],
    ["#8CB369", "#F4E285", "#F4A259", "#5B8E7D", "#BC4B51"],
    ["#9CF6F6", "#F3C98B", "#DAA588", "#C46D5E", "#F56960"],
    ["#2A0800", "#775144", "#C09891", "#BEA8A7", "#F4DBD8"],
    ["#442F38", "#9A9B73", "#C6B38E", "#E7F9A9", "#3D0814"],
    ["#ab9f98", "#BE5A38", "#353238", "#92140C", "#BE7C4D"],
    ["#3c1642", "#086375", "#1dd3b0", "#affc41", "#b2ff9e"],
    ["#5f0f40", "#9a031e", "#fb8b24", "#e36414", "#0f4c5c"],
    ["#780000", "#F0A202", "#0C5278", "#003049", "#669bbc"],
    ["#3d5a80", "#98c1d9", "#EFD28D", "#ee6c4d", "#293241"],
    ["#011627", "#F4E409", "#2ec4b6", "#e71d36", "#ff9f1c"],
    ["#321325", "#5f0f40", "#9a031e", "#cb793a", "#fcdc4d"],
    ["#090809", "#f40000", "#f44e3f", "#f4796b", "#f4998d"],
    ["#4f000b", "#720026", "#ce4257", "#ff7f51", "#ff9b54"],
    ["#fe5826", "#4b9d97", "#0d7876", "#076764", "#d8b593"],

    ["#E0F4FF", "#87C4FF", "#39A7FF", "#FFEED9","#7B3E19"],
    ["#0802A3", "#FF4B91", "#FF7676", "#FFCD4B","#a3c3d9"],
    ["#FF8080", "#FFCF96", "#F6FDC3", "#CDFAD5","#694A38"],
    ["#BFBDC1", "#6D6A75", "#37323E", "#DEB841","#DE9E36"],
    ["#6F61C0", "#A084E8", "#8BE8E5", "#D5FFE4","#FFCAB1"],
    ["#FDE5EC", "#FCBAAD", "#E48586", "#916DB3","#5B2333"],
    ["#1D5D9B", "#75C2F6", "#F4D160", "#FBEEAC","#D68FD6"],
    ["#F31559", "#FF52A2", "#FFB07F", "#FFECAF","#505168"],
    ["#3AA6B9", "#FFD0D0", "#FF9EAA", "#C1ECE4","#3A1772"],
    ["#0079FF", "#00DFA2", "#F6FA70", "#FF0060","#F49FBC"],
    ["#FF55BB", "#FFD3A3", "#FCFFB2", "#B6EAFA","#0B3954"],
    ["#FF6D60", "#F7D060", "#F3E99F", "#98D8AA","#444140"],
    ["#B6FFFA", "#98E4FF", "#80B3FF", "#687EFF","#12263A"],
    ["#27005D", "#9400FF", "#AED2FF", "#E4F1FF","#FFA5A5"],
    ["#793FDF", "#7091F5", "#97FFF4", "#FFFD8C","#FFFD8C"],
    ["#900C3F", "#C70039", "#F94C10", "#F8DE22","#016FB9"],
    ["#6F61C0", "#A084E8", "#8BE8E5", "#D5FFE4","#F05365"],
    ["#F11A7B", "#982176", "#3E001F", "#FFE5AD","#C8B8DB"],
    ["#071952", "#0B666A", "#35A29F", "#97FEED","#EEE5E9"],
    ["#FFB84C", "#F266AB", "#A459D1", "#2CD3E1","#302F4D"],
    ["#F6F1E9", "#FFD93D", "#FF8400", "#4F200D","#1F0318"],
    ["#7149C6", "#FC2947", "#FE6244", "#FFDEB9","#95B2B0"],
    ["#060047", "#B3005E", "#E90064", "#FF5F9E","#F6BDD1"],
    ["#3F0071", "#FB2576", "#332FD0", "#0002A1","#F9E0D9"],
    ["#00FFD1", "#31C6D4", "#FFFF00", "#FF1E1E","#ED6A5A"],
    ["#31E1F7", "#400D51", "#D800A6", "#FF7777","#F7A9A8"],
    ["#293462", "#1CD6CE", "#FEDB39", "#D61C4E","#EB5E28"],
    ["#3330E4", "#F637EC", "#FBB454", "#FAEA48","#BAD1CD"],
    ["#541690", "#FF4949", "#FF8D29", "#FFCD38","#CAD178"],
    ["#143F6B", "#F55353", "#FEB139", "#F6F54D","#06D6A0"],
    ["#293462", "#1CD6CE", "#FEDB39", "#D61C4E","#FB6376"],
    ["#3B0000", "#FF0000", "#FF95C5", "#FFF6CD","#4F6D7A"],
    ["#170055", "#3E00FF", "#AE00FB", "#B5FFD9","#AFD2E9"],
    ["#00EAD3", "#FFF5B7", "#FF449F", "#005F99","#2D3142"],
    ["#480032", "#005792", "#FC92E3", "#F2F4C3","#94A89A"],
    ["#72147E", "#F21170", "#FA9905", "#FF5200","#47A8BD"],
    ["#C67ACE", "#D8F8B7", "#FF9A8C", "#CE1F6A","#5A0B4D"],
    ["#26001B", "#810034", "#FF005C", "#FFF600","#F3F9D2"],
    ["#00BDAA", "#400082", "#FE346E", "#F1E7B6","#040404"],


]
var randomPallet;
var tiles;
var rnddiv = [1, 1.5, 2, 4, 8, 16];

var normalStroke = true; //
var withOffset = false;
var offsetLevel = 0; //random(1,5)
var maxoffset = 10;
var withRotation = false;

var tmp; //tempory variable


function setup() {
    createCanvas(deviceWidth, deviceHeight);
    angleMode(DEGREES);
    //colorMode(HSB);
    var rnd = Math.floor(Math.random() * (rnddiv.length - 1));
    tiles = floor(floor(width / step) / rnddiv[rnd]);
    console.log("tiles length", [tiles, rnddiv[rnd]]);

    ///console.log("Random", parseInt(Math.random() * colorpalletes.length));
    randomPallet = colorpalletes[parseInt(Math.random() * colorpalletes.length)];
    //console.log("arr", randomPallet);
    var bgIndex = parseInt(Math.random() * (randomPallet.length));
    //console.log("bg", bgIndex)
    background(randomPallet[bgIndex]);
    //console.log("bgIndex", bgIndex);
    randomPallet.splice(bgIndex, 1);
    //console.log("colorpall", randomPallet);
    lineWidth = 2;
    //console.log("lineWidth", lineWidth);

    //Type
    //Right now on random -> Later depends on MAC addresses


    //normal Strokes or thin strokes
    tmp = Math.random();
    if (tmp >= 0.7) {
        normalStroke = false;
    }

    //with offset or not
    tmp = Math.random();
    if (tmp >= 0.5) {
        withOffset = true;
        //if yes what kind of offset level
        offsetLevel = (Math.floor(Math.random() * 5)) + 1;
    }

    tmp = Math.random();
    if (tmp >= 0.5) {
        withRotation = true;
    }

    console.log("[normalStroke,withOffset,offsetLevel,withRotation]", [normalStroke, withOffset, offsetLevel, withRotation]);

    //type = Math.floor(random(0, 3));


}

function draw() {

    for (var y = 0; y < height; y += step) {
        for (var x = 0; x < width; x += step) {
            //translate(-step, -step)
            createLogo(x, y)
            //fill(randomPallet[parseInt(Math.random() * randomPallet.length)])
            //rect(x, y, 20, 20)
        }
    }

    for (var y = 0; y < height; y += step) {
        for (var x = 0; x < width; x += step) {
            fillLogo(x, y)//fills the up the empty spots
            //fill(randomPallet[parseInt(Math.random() * randomPallet.length)])
            //rect(x, y, 20, 20)
        }
    }
    //grainy()
    //drawLine2(0, 0, 100)

    noLoop();
    console.log("Image done")
    console.log("<picture>" + document.getElementById("defaultCanvas0").toDataURL())
}

function fillLogo(_x, _y) {
    var newLogo;
    var safe = false;
    newLogo = {
        //random place
        x: _x,
        y: _y,
        size: minSize
    }
    if (!detectCollision(newLogo)) {
        safe = true;
    }

    //ist es nicht sicher lass es aus
    if (!safe) {
        return;
    }

    logos.push(newLogo);

    if (withRotation) {
        drawLine2(newLogo.x, newLogo.y, newLogo.size)
    } else {
        drawLine(newLogo.x, newLogo.y, newLogo.size)
    }

}

function createLogo(_x, _y) {
    var newLogo;
    var safe = false;

    //Die Position eines Squares wird gefunden
    for (var tries = 0; tries < attempts; tries++) {
        //neues logo erstellt
        newLogo = {
            //random place
            x: _x,
            y: _y,
            size: Math.floor(Math.random() * tiles) * step
        }

        //gibt es eine collision
        if (detectCollision(newLogo)) {
            //newLogo.size = step;
            continue;
        } else {
            safe = true;
            break;
        }
    }

    //ist es nicht sicher lass es aus
    if (!safe) {
        return;
    }

    //Die größe wird verkleinert wenn es nicht reinpasst
    if (detectCollision(newLogo)) {
        for (var squareSize = minSize; squareSize < maxSize; squareSize += step) {
            newLogo.size = squareSize;
            if (detectCollision(newLogo)) {
                newLogo.size -= step;
                break;
            }
        }
    }

    //newLogo.size = (Math.floor(Math.random() * tiles) * step);
    /*while (!detectCollision(newLogo)) {
        newLogo.size = (Math.floor(Math.random() * tiles) * step);
    }*/



    logos.push(newLogo);
    //console.log("new logo", newLogo);
    //drawLine(newLogo.x, newLogo.y, newLogo.size);
    //fill("#ff0400");
    //fill(randomPallet[parseInt(Math.random() * randomPallet.length)]);
    //stroke(0)
    //rect(newLogo.x, newLogo.y, newLogo.size, newLogo.size)
    //var r = Math.random();
    if (withRotation) {
        drawLine2(newLogo.x, newLogo.y, newLogo.size)
    } else {
        drawLine(newLogo.x, newLogo.y, newLogo.size)
    }
    //


}

function detectCollision(newLogo) {
    for (var i = 0; i < logos.length; i++) { //loopen durch jeden kreis
        var other = logos[i];
        //console.log("other:", logos)
        if (newLogo.x < other.x + other.size &&
            newLogo.x + newLogo.size > other.x &&
            newLogo.y < other.y + other.size &&
            newLogo.size + newLogo.y > other.y) {

            //console.log("colls")
            return true;
        }
    }

    if (newLogo.x + (newLogo.size) > width ||
        newLogo.x < 0) {
        return true;
    }

    if (newLogo.y + (newLogo.size) > height ||
        newLogo.y < 0) {
        return true;
    }
    //console.log("no coll")
    return false;
}



function giveOffset(off) {
    var val;
    if (withOffset) {
        switch (offsetLevel) {
            case 5:
                val = Math.floor(random(-1, 1) * off)
                break;
            case 4:
                val = Math.floor(random(-1, 1) * (off * 0.1))
                break;
            case 3:
                val = Math.floor(random(-1, 1) * (off * 0.07))
                break;
            case 2:
                val = Math.floor(random(-1, 1) * (off * 0.05))
                break;
            case 1:
                val = Math.floor(random(-1, 1) * (off * 0.01))
                break;
            default:
                val = 0;
            //
            //return Math.floor(random(-1, 1) * (off * 0.07))
            //return Math.floor(random(-1, 1) * (off * 0.05))
            //return Math.floor(random(-1, 1) * (off * 0.1))

        }
    } else {
        return 0
    }

    return val
}


function drawLine(x, y, size) {
    //strokeCap(ROUND);
    var normal = noise(xoff) >= 0.5; //Math.random() >= 0.5;
    //var newLines;
    var offset;
    if (normal) {
        if (normalStroke) {
            strokeWeight(size * 0.1); //
            //console.log("strokeweight", size * 0.1)  
        } else {
            strokeWeight(lineWidth); //size * 0.1
        }

        stroke(randomPallet[parseInt(Math.random() * randomPallet.length)]);
        //rotate(45)
        //translate(-20, -20)
        //offset = Math.floor(random(-1, 1) * maxoffset)
        //console.log(offset);//parseInt(Math.random() * maxoffset);
        /*if(Math.random()>=0.5){
            offset = offset *-1)
        }*/
        line(x + (size * 0.1), y + (size * 0.1) + giveOffset(size), x + size - (size * 0.1), y + (size * 0.1) + giveOffset(size));
        //(Math.random() * 360, Math.random() * 100, Math.random() * 100);
        line(x + (size * 0.2), y + (size * 0.5) + giveOffset(size), x + (size * 0.8), y + (size * 0.5) + giveOffset(size));
        //stroke(Math.random() * 360, Math.random() * 100, Math.random() * 100);
        line(x + (size * 0.1), y + size - (size * 0.1) + giveOffset(size), x + size - (size * 0.1), y + size - (size * 0.1) + giveOffset(size));

        newLines = [{
            x: [x, y],
            y: [size, y]
        },
        {
            x: [x + size * 0.2, y + size * 0.4],
            y: [size * 0.8, y + size * 0.4]
        },
        {
            x: [x, y + size * 0.8],
            y: [size, y + size * 0.8]
        }
        ]

        //console.log("newLines", newLines)


    } else {
        if (normalStroke) {
            strokeWeight(size * 0.1); //
            //console.log("strokeweight", size * 0.1)  
        } else {
            strokeWeight(lineWidth); //size * 0.1
        }
        stroke(randomPallet[parseInt(Math.random() * randomPallet.length)]);
        //console.log("color", r)
        //translate(-20, -20)
        line(x + (size * 0.1) + giveOffset(size), y + (size * 0.1), x + (size * 0.1) + giveOffset(size), y + size - (size * 0.1));
        //stroke(Math.random() * 360, Math.random() * 100, Math.random() * 100);
        line(x + (size * 0.5) + giveOffset(size), y + (size * 0.2), x + (size * 0.5) + giveOffset(size), y + (size * 0.8));
        //stroke(Math.random() * 360, Math.random() * 100, Math.random() * 100);
        line(x + size - (size * 0.1) + giveOffset(size), y + (size * 0.1), x + size - (size * 0.1) + giveOffset(size), y + size - (size * 0.1));

        newLines = [{
            x: [x, y],
            y: [x, y + size]
        },
        {
            x: [x + size * 0.5, y + size * 0.2],
            y: [x + size * 0.5, y + size * 0.8]
        },
        {
            x: [x + size, y],
            y: [x + size, y + size]
        }
        ]

        //console.log("newLines", newLines)

    }
    xoff += 0.2;
}


//Draws line with rotation
function drawLine2(x, y, size) {
    //strokeCap(ROUND);
    var normal = noise(xoff) >= 0.5; //Math.random() >= 0.5;
    //var newLines;
    var offset;
    if (normal) {
        if (normalStroke) {
            strokeWeight(size * 0.1); //
            //console.log("strokeweight", size * 0.1)  
        } else {
            strokeWeight(lineWidth); //size * 0.1
        }
        stroke(randomPallet[parseInt(Math.random() * randomPallet.length)]);
        //rotate(45)
        //translate(-20, -20)
        //offset = Math.floor(random(-1, 1) * maxoffset)
        //console.log(offset);//parseInt(Math.random() * maxoffset);
        /*if(Math.random()>=0.5){
            offset = offset *-1)
        }*/
        //rect(x, y, size)
        line(x + (size * 0.1), y + (size / 2), x + (size / 2), y + (size * 0.1) + giveOffset(size));
        //(Math.random() * 360, Math.random() * 100, Math.random() * 100);
        line(x + (size * 0.40), y + (size * 0.60) + giveOffset(size), x + (size * 0.60), y + (size * 0.40) + giveOffset(size));
        //stroke(Math.random() * 360, Math.random() * 100, Math.random() * 100);
        line(x + (size / 2), y + size - (size * 0.1) + giveOffset(size), x + size - (size * 0.1), y + (size / 2) + giveOffset(size));

        newLines = [{
            x: [x, y],
            y: [size, y]
        },
        {
            x: [x + size * 0.2, y + size * 0.4],
            y: [size * 0.8, y + size * 0.4]
        },
        {
            x: [x, y + size * 0.8],
            y: [size, y + size * 0.8]
        }
        ]

        //console.log("newLines", newLines)


    } else {
        if (normalStroke) {
            strokeWeight(size * 0.1); //
            //console.log("strokeweight", size * 0.1)  
        } else {
            strokeWeight(lineWidth); //size * 0.1
        }
        stroke(randomPallet[parseInt(Math.random() * randomPallet.length)]);
        //console.log("color", r)
        //translate(-20, -20)
        //rect(x, y, size)
        line(x + (size * 0.1) + giveOffset(size), y + (size * 0.5), x + (size * 0.5) + giveOffset(size), y + size - (size * 0.1));
        //stroke(Math.random() * 360, Math.random() * 100, Math.random() * 100);
        line(x + (size * 0.4) + giveOffset(size), y + (size * 0.4), x + (size * 0.6) + giveOffset(size), y + (size * 0.6));
        //stroke(Math.random() * 360, Math.random() * 100, Math.random() * 100);
        line(x + (size / 2) + giveOffset(size), y + (size * 0.1), x + size - (size * 0.1) + giveOffset(size), y + (size / 2));

        newLines = [{
            x: [x, y],
            y: [x, y + size]
        },
        {
            x: [x + size * 0.5, y + size * 0.2],
            y: [x + size * 0.5, y + size * 0.8]
        },
        {
            x: [x + size, y],
            y: [x + size, y + size]
        }
        ]

        //console.log("newLines", newLines)

    }
    xoff += 0.2;
}


// function keyPressed() {
//     if (key === 's') {
//         save('image.png');
//     }
// }  