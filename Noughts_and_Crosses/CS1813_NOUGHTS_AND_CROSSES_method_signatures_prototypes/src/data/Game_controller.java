package data;

import data.shared_types_and_configuration.Config;

abstract public class Game_controller {

		 abstract static class GameController {
			 
			 abstract public GameController(Config cfg);
			 
			 abstract private void Difficulty(Difficulty d);
			 
		 }
}
