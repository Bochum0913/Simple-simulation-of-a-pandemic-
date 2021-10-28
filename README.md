# Simple-simulation-of-a-pandemic-
A very simple simulation of a pandemic. It should let user enter
 * 			immunity status of individuals and population size.
 * 			Simulation starts with one(1) patient zero and there is a chance of infection
 * 		    when persons collide.
 *          Chances are determined by:
 *          1) If an infected person collides with an uninfected person who has no immunity, then there is an 80% chance that the disease will be passed on to the uninfected person.
 *			2) If an infected person collides with an uninfected person who has had ONE shot of the vaccine, then there is a 40% chance that the uninfected person will be passed on to the uninfected person.
 *			3) If an infected person collides with an uninfected person who has had BOTH shots of the vaccine, then there is only a 10% chance that the uninfected person will be passed on to the uninfected person.
 *			4) If an infected person collides with a person who has had the disease and recovered ( a green dot on the screen shot) then there is only a 10% chance that the disease will be passed on to the uninfected person.
 *			5) An infected person has a 10% chance of dying from the disease.
