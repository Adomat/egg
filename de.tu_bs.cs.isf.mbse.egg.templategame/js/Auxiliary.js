function getPixelOffsetFromScroll(scroll, mapSize) {
	var returnVector = function () {  return {  x: null,  y: null  };  };
	
	var leftGap = Math.min(0, scroll.x * mapSize.x * blockSize - width/2 + blockSize/2);
	var rightGap = (-1) * Math.min(0, mapSize.x * blockSize - scroll.x * mapSize.x * blockSize - width/2 + blockSize/2);

	var lowerGap = Math.min(0, (1-scroll.y) * mapSize.y * blockSize - height/2 + blockSize);
	var upperGap = (-1) * Math.min(0, mapSize.y * blockSize - (1-scroll.y) * mapSize.y * blockSize - height/2);

	returnVector.x = leftGap;
	returnVector.y = lowerGap;

	if(rightGap != 0)
		returnVector.x = rightGap;
	if(upperGap != 0)
		returnVector.y = upperGap;

	if(leftGap != 0 && rightGap != 0)
		returnVector.x = 0;
	if(lowerGap != 0 && upperGap != 0)
		returnVector.y = 0;

	if(mapSize.y * blockSize < height) {
		returnVector.y = lowerGap;
	}
	if(mapSize.x * blockSize < width) {
		returnVector.x = width/2 - mapSize.x * blockSize / 2 + leftGap;
	}
	
//	if(Math.round(Math.random() * 1000) == 1) {
//		console.log("leftGap", leftGap);
//		console.log("rightGap", rightGap);
//		console.log("sum", leftGap+rightGap);
//		console.log("--------------------");
//	}
	
	return returnVector;
}









function hexToRgbA(hex, opacity){
    var c;
    if(/^#([A-Fa-f0-9]{3}){1,2}$/.test(hex)){
        c= hex.substring(1).split('');
        if(c.length== 3){
            c= [c[0], c[0], c[1], c[1], c[2], c[2]];
        }
        c= '0x'+c.join('');
        return 'rgba('+[(c>>16)&255, (c>>8)&255, c&255].join(',')+','+opacity+')';
    }
    throw new Error('Bad Hex');
}