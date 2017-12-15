var keyDownArray = [];


function handleKeyDownEvent(event) {
	if(!keyDownArray.includes(event.keyCode))
		keyDownArray.push(event.keyCode);
	
	passKeyEventToPages(event);
}

function handleKeyUpEvent(event) {
	var index = keyDownArray.indexOf(event.keyCode);

	if (index > -1) {
		keyDownArray.splice(index, 1);
	}
}