This program is designed to find the most "efficient" recipe for each item. The reason for the double quotes is because "efficient" is a bit subjective.

In this case the program bases it off of a weighted point system where recipes that favor more abundant resources are picked. The weights are calculated via `(Raw Resource Rate) * 10,000 / (Max Resource Extraction Rate)`.

This program also currently doesn't account for chains where byproducts from one step are used in a later step.

This program should be ran from the command line, has no GUI, and takes no input.
