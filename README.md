# Morechids
Unlimited customizable Orechid variants for botanical progression


# The following documentation does not reflect the existing functionality of the mod. It is my projection of the future functionality that I am working on implementing. Hopefully it'll be a reality soon. Don't get too excited.


# Flower Creation

## Defining Flowers

### Creating Unique Morechid IDs

`morechids.json` is the configuration file used to define custom Morechid flowers. Flowers are specified as fields of a JSON object where the key is the unique identifier for the flower. The value of the key is another JSON object containing the configured option values for the flower. Note that the unique identifiers are *not case sensitive*. Additionally, **existing botania flower identifiers are off limits**.

For example, the following JSON structure would create two flowers with unique names `myflower` and `my_second_flower`, each with default values for all options.
```json
{
    "myflower": {},
    "my_SECOND_fLoWeR": {}
}
```

### Supplying Textures and Resources
Each custom flower can have external resources provided just like any other block. The resource path is based on the unique name you use to define your flower. For the example above, the resource paths for the two flowers would be `morechids:myflower` and `morechids:my_second_flower`. Resources can be provided with a resource pack or using a mod such as **ResourceLoader**.

## Configuring Your Morechids

Each Morechid you create can have each parameter individually configured. Morechids can be configured to cost mana like an Orechid, time like a Pure Daisy, or some combination of the two. Any parameter can be configured to act differently in a Garden Of Glass world simply by appending "GOG" to the end of the parameter name. If not explicitly specified, all "GOG" parameters will default to the same value as the corresponding non-"GOG" parameter.

**Example configuration for mimicking Botania's Orechid behavior:** (minus the recipes)
*(Note: recipes must be configured separately through CraftTweaker)*
```json
{
    "orechid_clone": {
        "manaCost": 17500,
        "manaCostGOG": 700,
        "timeCost": 0,
        "cooldown": 100,
        "cooldownGOG": 2,
        "color": "818181",
        "range": 5,
        "rangeY": 3,
        "playSound": true
    }
}
```

**Example configuration for mimicking Botania's Pure Daisy behavior:**
*(Note: recipes must be configured separately through CraftTweaker)*
```json
{
    "puredaisy_clone": {
        "manaCost": 0,
        "timeCost": 1200,
        "cooldown": 0,
        "color": "FFFFFF",
        "range": 1,
        "rangeY": 0,
        "playSound": false
    }
}
```

### Configurable Parameters:

* `manaCost` / `manaCostGOG`: ***Integer*** [0 ~ ∞] *(default: 17500)*
  * The amount of mana required to perform a conversion.

* `timeCost` / `timeCostGOG`: ***Integer*** [0 ~ ∞] *(default: 0)*
  * The number of ticks a block must remain in the flower's range in order to be converted.
  * If `manaCost` is also specified, mana will not be consumed until the instant the block is converted.

* `cooldown` / `cooldownGOG`: ***Integer*** [0 ~ ∞] *(default: 100)*
  * The minimum number of ticks that must pass between block conversions.
  * If this value is 0, the flower will attempt to convert all blocks that meet the `timeCost` requirements in the same tick. If `manaCost` is greater than 0, attempts will be made in a random order, and attempts will stop if the flower runs out of mana.

* `range` / `rangeGOG`: ***Integer*** [0 ~ ∞] *(default: 5)*
  * The number of blocks horizontally from the flower in which a block will be detected.
  * **This number can cause performance issues.** See the **Performance Concerns** section for more details.

* `rangeY` / `rangeYGOG`: ***Integer*** [0 ~ ∞] *(default: 3)*
  * The number of blocks vetically from the flower in which a block will be detected.
  * **This number can cause performance issues.** See the **Performance Concerns** section for more details.

* `maxMana` / `maxManaGOG`: ***Integer*** [`manaCost` ~ ∞] *(default: same as `manaCost`)*
  * The maximum amount of mana that can be stored in this flower's buffer. A number larger than `manaCost` may allow for multiple blocks to be converted before the flower runs out of mana.
  * If `manaCost` is 0, this value will be ignored.

* `particleColor` / `particleColorGOG`: ***String*** *(default: randomized by flower name)*
  * A hex value as a string (ex: "FF0000") representing the color of sparkles coming off the flower when active, and the default flower color if no other resource is provided.
  * If not specified, a random color will be selected. This color is guaranteed to be consistent for any given flower name.

* `playSound` / `playSoundGOG`: ***Boolean*** *(default: true)*
  * If true, the Orechid **BANG!** sound will play when a block is converted.

* `rangeCheckInterval` / `rangeCheckIntervalGOG`: ***Integer*** [1 ~ `cooldown`] *(default: `cooldown`)*
  * The number of ticks to wait between checks for newly placed blocks.
  * **This number can cause performance issues.** See the **Performance Concerns** section for more details.

* `blockBreakParticles` / `blockBreakParticlesGOG`: ***Boolean*** *(default: true)*
  * If true, block breaking particles will appear when a block is converted.

### Performance Concerns and Behavior Oddities
In order to locate blocks to convert, flowers must search the surrounding area for valid candidates. This requires checking every block within the flower's range, meaning a volume of `(2*range + 1)^2 + 2*rangeY` blocks. For a flower with `range` of 5 and `rangeY` of 3 (like the *default* Orechid), this would mean ***127 blocks every tick***.

Setting the `rangeCheckInterval` value to 20, for example, would allow this check to occur once every second, rather than once every tick. There are a few optimizations in the code to prevent this check from occurring when it is not necessary (such as if a flower has less mana than its `manaCost`), but for the most part please keep this in mind when selecting a flower range.

The `rangeCheckInterval` value *will* have a noticeable effect on the flower's behavior, in that it will potentially cause some variation in the effective `cooldown` value for a flower. If a block is placed in the tick right after the range check last occurred, it will not be detected until the next range check occurs. Because of this, for a flower with a `timeCost` less than its `rangeCheckInterval`, it will not detect the block until the next range check occurs *regardless of whether the `cooldown` is ready or not*.

# Adding Recipes
Adding recipes to a Morechid is very straight forward and simply requires the use of a **CraftTweaker** script.

### Commands
* `mods.morechids.Registry.getFlower(String identifier)`; returns `MorechidDefinition`
  * Returns a `MorechidDefinition` object used to define recipes for the flower with the given `identifier`.

* `MorechidDefinition::addRecipe(IIngredient input, IIngredient output, double weight)`
  * Adds a conversion from the `input` block(s) to the `output` block(s) with the given `weight` used to specify the probability of this recipe being selected as a conversion. Each call to `addRecipe` adds a new conversion entry for each input block into the recipe set for this flower. If `output` contains an array of items or an Ore Dictionary entry, the recipe entry will contain the *entire set of all blocks in `output`*, rather than each output block being its own separate entry. If an entry with an array of outputs is selected for a conversion, the actual converted block is selected uniformly at random from the array.
  * If the same `output` is added for an `input` block again in a different call to `addRecipe()`, a new recipe entry will be created for that output block *in addition to any existing entries that may contain the same output block*.
  * `input` and `output` can be any item or liquid that has a block representation, an Ore Dictionary entry containing blocks, or an array of items that have block representations.
  * `weight` is a positive number added to each recipe entry. The probability of any entry being selected for a given `input` block is `weight` of the entry divided by the sum of the `weight` values for every recipe entry associated with that `input` block.

* `MorechidDefinition::removeRecipe(IIngredient input, IIngredient output)`
  * Removes the ability for any of the `input` block(s) to be converted to any of the `output` block(s). This is applied to *ALL* recipe entries associated with this flower. Any recipe entries for each `input` block which contain any `output` block will have that `output` block removed from its set of possible outputs for the entry, but *the entry will not be removed entirely* unless this removal causes the entry to be completely empty.
  * `input` and `output` can be any item or liquid that has a block representation, an Ore Dictionary entry containing blocks, or an array of items that have block representations.

* `MorechidDefinition::removeOutput(IIngredient output)`
  * Removes the specified `output` block(s) as an output for all recipe entries that may contain it, regardless of input block.
  * `output` can be any item or liquid that has a block representation, an Ore Dictionary entry containing blocks, or an array of items that have block representations.

* `MorechidDefinition::removeInput(IIngredient input)`
  * Removes the specified `input` block(s) as an input for all recipe entries that may contain it, regardless of output block.
  * `input` can be any item or liquid that has a block representation, an Ore Dictionary entry containing blocks, or an array of items that have block representations.

### Example CraftTweaker code
Here is an example just to show how the functions can be used. Yes, I know this is a pretty useless script. Hopefully you'll make better choices in your recipes.
```js
// Get the flower instance and save it in a variable
var myFlower = mods.morechids.Registry.getFlower("myflower");
// Add some stone conversions to the flower
myFlower.addRecipe(<minecraft:stone>, <ore:oreIron>, 100);
myFlower.addRecipe(<minecraft:stone>, <minecraft:dirt>, 5);
myFlower.addRecipe(<minecraft:stone>, <minecraft:cobblestone>, 5);

// Add a conversion for any glass or stained glass to convert to any sandstone
myFlower.addRecipe(<ore:blockGlass>, <ore:sandstone>, 1);

// Remove a specific sandstone variant from the glass->sandstone conversion
myFlower.removeRecipe(<ore:blockGlass>, <minecraft:sandstone:1>);

// Add that conversion back only for regular minecraft glass blocks but not stained glass blocks.
myFlower.addRecipe(<minecraft:blockGlass>, <minecraft:sandstone:1>, 0.2);

// Remove dirt as a possible output of any recipe
myFlower.removeOutput(<minecraft:dirt>);

// Remove stone as a possible input for any recipe
myFlower.removeInput(<minecraft:stone>);




// Get another flower instance and set its recipes
var mySecondFlower = mods.morechids.Registry.getFlower("my_second_flower");
mySecondFlower.addRecipe(...);
mySecondFlower.addRecipe(...);
mySecondFlower.addRecipe(...);

// Et cetera
```
