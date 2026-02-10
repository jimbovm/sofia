import com.github.jimbovm.isobel.actor.geography.ExtensiblePlatform
import com.github.jimbovm.isobel.actor.geography.SingletonObject
import com.github.jimbovm.isobel.actor.geography.Castle
import com.github.jimbovm.isobel.actor.population.Character
import com.github.jimbovm.isobel.common.AreaHeader.Platform

/**
 * The bounding box of an actor, i.e. how many blocks the object it
 * spawns takes up in-game.
 * 
 * A dimension of a tangible object is always greater than zero (i.e. an 
 * fills the space of one block minimum). Intangible actors, such as
 * scroll locks, loops, exit pointers etc. have no "size", so both 
 * dimensions are 0.
 */
data class Dimensions(
	val width: Int,
	val height: Int
) {

	companion object {

		/**
		 * Return the dimensions of an extensible platform, given the
		 * platform type injected.
		 */
		fun of(extensiblePlatform: ExtensiblePlatform, platform: Platform): Dimensions {

			when (platform) {
				Platform.CANNON -> return Dimensions(1, extensiblePlatform.extent + 1)
				else -> return Dimensions(extensiblePlatform.extent + 1, 1)
			}
		}

		/** Return the dimensions of a character. */
		fun of(character: Character): Dimensions {
			when (character.type) {
				Character.Type.BOWSER -> return Dimensions(2, 3)
				Character.Type.HAMMER_BRO -> return Dimensions(1, 3)
				Character.Type.TOAD_PEACH,
				Character.Type.GREEN_TROOPA,
				Character.Type.RED_TROOPA,
				Character.Type.GREEN_PARATROOPA_HOP,
				Character.Type.GREEN_PARATROOPA_HOVER,
				Character.Type.RED_PARATROOPA -> return Dimensions(1, 2)

				else -> return Dimensions(1, 1);
			}
		}

		fun of(castle: Castle): Dimensions {

			when (castle.size) {
				Castle.Size.LARGE -> return Dimensions(9, 9)
				else -> return Dimensions(5, 5)
			}
		}

		/**
		 * Return a constant value of Dimensions(1, 1).
		 */
		fun of(singletonObject: SingletonObject): Dimensions {
			return Dimensions(1, 1)
		}


	}
}