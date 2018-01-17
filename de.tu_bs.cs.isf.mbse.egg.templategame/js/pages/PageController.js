var currentPage = null;
var pages; 


function setupPages() {
    blockSize = 75;
    startPageKey = "menu";
    
	pages = [];

	// MENU PAGE
	var menuPage = new MenuPage();
	menuPage.setPageKey("menu");
	menuPage.setBackgroundImage("images/background.png");
	menuPage.setBackgroundColor("6699ff");
	menuPage.addLogoImage("images/title/title1.png");
	menuPage.addLogoImage("images/title/title2.png");
	menuPage.logoAnimationSpeed = 500;
    menuPage.addButton("Start Game", "level1");
    menuPage.addButton("How does this work?", "tutorial");

	pages.push(menuPage);

	// TUTORIAL PAGE
	var tutorialPage = new TextPage("How to move the poorly drawn lines");
	tutorialPage.setPageKey("tutorial");
	tutorialPage.newPageKey = "menu";
	tutorialPage.setBackgroundImage("images/background.png");
	tutorialPage.addParagraph("Have you tried the Arrow keys? FFS...");
	tutorialPage.addParagraph("");
//	tutorialPage.addParagraph("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
//	tutorialPage.addParagraph("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
//	tutorialPage.addParagraph("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
//	tutorialPage.addParagraph("Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.");
//	tutorialPage.addParagraph("");
	tutorialPage.addParagraph("HAVE FUN!");
	
	pages.push(tutorialPage);
    
    var dirtBlock = new Block();
    dirtBlock.addImage("images/blocks/dirt.png");
    
    var grassBlock = new Block();
    grassBlock.addImage("images/blocks/grass.png");
    grassBlock.addImage("images/blocks/grass2.png");
    grassBlock.animationSpeed = 500;

	// LEVEL PAGE 1
	var level1Page = new LevelPage();
	level1Page.setPageKey("level1");
	level1Page.setBackgroundImage("images/background.png");
	level1Page.setBackgroundColor("6699ff");
    
	for(var i=0; i<10; i++) {
		level1Page.addBlock(dirtBlock, -1, i);
	}
	for(var i=14; i<25; i++) {
		level1Page.addBlock(dirtBlock, i, i-10);
	}
	level1Page.addBlock(grassBlock, 25, 15);
	for(var j=0; j<2*25; j+=25) {
		for(var i=0; i<10; i++) {
			level1Page.addBlock(dirtBlock, i+j, 0);
		}
		for(var i=0; i<10; i++) {
			level1Page.addBlock(grassBlock, i+j, 4);
		}
		for(var i=13; i<25; i++) {
			level1Page.addBlock(grassBlock, i+j, 0);
		}
		
		level1Page.addBlock(grassBlock, 9+j, 1);
		level1Page.addBlock(dirtBlock, 12+j, 0);
		level1Page.addBlock(grassBlock, 12+j, 1);
	}
	
	var hero = new HeroCharacter();
	hero.setCollisionBox(30, 130);
	hero.animationSpeed = 200;
	hero.life = 100;
	hero.speed = 5;
	hero.jumpPower = 35;
	hero.addIdleImage("images/character/idle.png");
	hero.addRunImage("images/character/run1.png");
	hero.addRunImage("images/character/run2.png");
	hero.addJumpImage("images/character/jump.png");
	level1Page.addHero(hero, 5, 1);
	
	level1Page.gravity = 10;
	level1Page.addExitGate("level2", 24, 15);

	pages.push(level1Page);

	// LEVEL PAGE 2
	var level2Page = new LevelPage();
	level2Page.setPageKey("level2");
	level2Page.setBackgroundImage("images/background.png");
	level2Page.setBackgroundColor("6699ff");
	
	var newBlock;
	for(var i=0; i<10; i++) {
		level2Page.addBlock(grassBlock, i, 0);
	}
	
	var hero = new HeroCharacter();
	hero.setCollisionBox(30, 130);
	hero.animationSpeed = 200;
	hero.life = 100;
	hero.speed = 5;
	hero.jumpPower = 35;
	hero.addIdleImage("images/character/idle.png");
	hero.addRunImage("images/character/run1.png");
	hero.addRunImage("images/character/run2.png");
	hero.addJumpImage("images/character/jump.png");
	level2Page.addHero(hero, 5, 1);
	
	level2Page.gravity = 5;

	pages.push(level2Page);
}


function drawCurrentPage() {
	if(currentPage == null) {
		switchPage(startPageKey);
	}
	
	currentPage.draw();
}


function passKeyEventToPages(event) {
    currentPage.keyPressed(event);
	/*if(currentPage.pageKey == "menu") {
		currentPage.keyPressed(event);
	}
	else if(currentPage.pageKey == "tutorial") {
		currentPage.keyPressed(event);
	}*/
}


function passTickToCurrentPage() {
	currentPage.executeTick();
}



function switchPage(pageKey) {
	for(var i=0; i<pages.length; i++) {
		if(pages[i].getPageKey() == pageKey) {
			currentPage = pages[i];
		}
	}
}

