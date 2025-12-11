## AhkToBash

This is a program to convert Guild Wars 2 Music Autohotkey scripts (numpad and sleep only!) to bash scripts that use `ydotool` for key input.

This currently only works for 2 commands (maybe 4 if you stretch it), either `SendInput` or `Sleep`. Conversion for `SendInput` commands only work correctly for Numpad keys (Currently just add 200 to the keycode).

If `SendInput` doesn't specify a down/up stroke, two commands are generated - not sure how this would work though.

An additional 5 seconds of sleep is added at the front of the bash script to allow you to (probably) alt-tab over to your game.

## Why is it in Java?
I have a java exam tomorrow as of writing this. Need some practice using Swing, Stream processing, and Functional programming before going in.

## Usage

either run with `java -jar 'ahktobash-jar-with-dependencies.jar'`

or install as flatpak with `flatpak install --user com.github.ykshek.AhkToBash.flatpak`

output is stored in `~/output` (yes I know I should be using xdg standards but oh well).

## Note

You may need to manually create an `~/output/` folder to run the program.
