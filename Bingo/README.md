# Bingo -  a minigame for PaperSpigot 1.15.2

## About
This minigame is a competition between 6 Teams which have to find 9 random generated items. </br>
The first team which found all nine items wins.

## Commands
`/items` - Check all nine random generated items <br>
`/skipitem` - Skip an item which is to difficult (`permission: bingo.skipitem or op`) </br>
`/backpack | /bp` - opens the team backpack

## Config
Currently, there is no config available (planned for update 1.4) </br>
If you are a developer, you can change following directly in the code: </br>

`de.fel1x.bingo.generation.ItemGenerator.java` - Change `BingoDifficulty` in line 16 </br>
to either **EASY**, **MIDDLE** or **HARDCORE**

`de.fel1x.bingo.objects.BingoTeam.java` - Here you are able to change teamsize, teamcolor, teamname </br>
 and the size of the backpack inventory
 
`de.fel1x.bingo.listener.player.JoinListener.java` - If you want to change the required players to start the game, </br>
you can change following formula: (line 37) </br>

`int neededPlayers = (BingoTeam.RED.getTeamSize() * 2) - bingo.getData().getPlayers().size();` (default / result: min. 4 Players) </br>

## Contribute 
If you want tho contribute, fork this project, grab some stuff from the `Projects` page on GitHub or implement your own ideas </br>
and send in a pull request :)

## License
This project uses the following license:
![License:](https://licensebuttons.net/l/by-nc-sa/4.0/88x31.png "Attribution-NonCommercial-ShareAlike 4.0 International") </br>
Read more **[here](https://creativecommons.org/licenses/by-nc-sa/4.0/)**


