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
	menuPage.setBackgroundColor("#6699ff");
    menuPage.textSize = 25;
	menuPage.textStyle = "#942020";
	menuPage.addLogoImage("images/title/title1.png");
	menuPage.addLogoImage("images/title/title2.png");
	menuPage.logoAnimationSpeed = 500;
    menuPage.addButton("Start Game", "level1");
    menuPage.addButton("How does this work?", "tutorial");

	pages.push(menuPage);

	// TUTORIAL PAGE
	var tutorialPage = new TextPage();
    //tutorialPage.title = "How to move the poorly drawn lines";
	tutorialPage.setPageKey("tutorial");
    tutorialPage.textSize = 15;
	tutorialPage.textStyle = "#942020";
	tutorialPage.newPageKey = "menu";
	tutorialPage.setBackgroundImage("images/background.png");
	tutorialPage.addParagraph("Have you tried the Arrow keys? FFS...");
	tutorialPage.addParagraph("");
	tutorialPage.addParagraph("HAVE FUN!");
    
	pages.push(tutorialPage);
    
    var dirtBlock = new Block();
    dirtBlock.addImage("images/blocks/dirt.png");
    
    var grassBlock = new Block();
    grassBlock.addImage("images/blocks/grass.png");
    grassBlock.addImage("images/blocks/grass2.png");
    grassBlock.animationSpeed = 500;
	
	var hero = new HeroCharacter();
	hero.setCollisionBox(90, 90);
	hero.animationSpeed = 150;
	hero.life = 100;
    hero.strength = 10;
	hero.speed = 10;
	hero.jumpPower = 50;
    //hero.showCollisionBox = true;
	hero.addIdleImage("images/enemy/hero_idle.png");
	hero.addJumpImage("images/enemy/hero_jump.png");
	hero.addRunImage("images/enemy/hero01.png");
	hero.addRunImage("images/enemy/hero02.png");
	hero.addRunImage("images/enemy/hero03.png");
	hero.addRunImage("images/enemy/hero04.png");
	hero.addRunImage("images/enemy/hero05.png");
	hero.addRunImage("images/enemy/hero06.png");
	/*hero.addIdleImage("images/character/idle.png");
	hero.addRunImage("images/character/run1.png");
	hero.addRunImage("images/character/run2.png");
	hero.addJumpImage("images/character/jump.png");*/
	
	var enemy = new EnemyCharacter();
    enemy.name = "enemy";
    enemy.movable = true;
	enemy.setCollisionBox(90, 90);
	enemy.animationSpeed = 150;
	enemy.life = 100;
    enemy.strength = 10;
	enemy.speed = 3;
	enemy.jumpPower = 40;
    //enemy.showCollisionBox = true;
	enemy.addIdleImage("images/enemy/hero_idle.png");
	enemy.addJumpImage("images/enemy/hero_jump.png");
	enemy.addRunImage("images/enemy/hero01.png");
	enemy.addRunImage("images/enemy/hero02.png");
	enemy.addRunImage("images/enemy/hero03.png");
	enemy.addRunImage("images/enemy/hero04.png");
	enemy.addRunImage("images/enemy/hero05.png");
	enemy.addRunImage("images/enemy/hero06.png");
    
    var tentacleAlien = new EnemyCharacter();
    tentacleAlien.name = "tentacleAlien";
    tentacleAlien.showCollisionBox = true;
	tentacleAlien.setCollisionBox(50, 50);
	tentacleAlien.jumpPower = 50;
	tentacleAlien.life = 3;
	tentacleAlien.speed = 2;
	tentacleAlien.strength = 2;
	tentacleAlien.addRunImage("images/tentacle_alien_idle.png");
	tentacleAlien.addRunImage("images/tentacle_alien_run_01.png");
	tentacleAlien.addRunImage("images/tentacle_alien_run_02.png");
	tentacleAlien.addRunImage("images/tentacle_alien_run_03.png");
	tentacleAlien.addRunImage("images/tentacle_alien_run_04.png");
	tentacleAlien.addJumpImage("images/tentacle_alien_idle.png");
	tentacleAlien.addJumpImage("images/tentacle_alien_run_01.png");
	tentacleAlien.addJumpImage("images/tentacle_alien_run_02.png");
	tentacleAlien.addJumpImage("images/tentacle_alien_run_03.png");
	tentacleAlien.addJumpImage("images/tentacle_alien_run_04.png");
	tentacleAlien.addIdleImage("images/tentacle_alien_idle.png");
	tentacleAlien.animationSpeed = 200;
    

	// LEVEL PAGE 1
	var level1Page = new LevelPage();
    level1Page.blockSize = 50;
	level1Page.setPageKey("level1");
    level1Page.deathMessage = "GAME OVER";
    level1Page.deathMessageParagraph = "Press \'Enter\' to return to the main menu";
    level1Page.winMessage = "CONGRATULIATIONS";
    level1Page.winMessageParagraph = "You won the Game.\nPress \'Enter\' to return to the main menu";
	level1Page.setBackgroundImage("images/grassland.png");
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
    
	level1Page.addEnemy(tentacleAlien, 25, 1);
	level1Page.addHero(hero, 20, 1);
	level1Page.gravity = 15;
	level1Page.addExitGate("level2", 50, 1);
    level1Page.addFinishBlock(1, 1);

	pages.push(level1Page);

	// LEVEL PAGE 2
	var level2Page = new LevelPage();
    level2Page.blockSize = 200;
	level2Page.setPageKey("level2");
	level2Page.setBackgroundImage("images/background.png");
	level2Page.setBackgroundColor("6699ff");
	
	for(var i=0; i<60; i++) {
		level2Page.addBlock(dirtBlock, i, 0);
	}
	for(var i=0; i<60; i++) {
		level2Page.addBlock(grassBlock, i, 1);
	}
    
	for(var i=15; i<45; i++) {
	   //level2Page.addEnemy(enemy, i, 3);
	}
	level2Page.addHero(hero, 1, 3);
	level2Page.gravity = 2;

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
            
            if(currentPage instanceof LevelPage) {
                blockSize = currentPage.blockSize;
            }
		}
	}
}

