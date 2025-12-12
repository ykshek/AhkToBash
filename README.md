## AhkToBash

This is a program to convert Guild Wars 2 Music Autohotkey scripts (currently numpad keys and sleep only!) to bash scripts that use [ydotool](https://github.com/ReimuNotMoe/ydotool) for key input.

This currently only works for 2 commands (maybe 4 if you stretch it), either `SendInput` or `Sleep`. Conversion for `SendInput` commands only work correctly for Numpad keys (~~Currently just add 200 to the keycode~~ Now uses HashMap as a LUT to convert keycodes - will add more keycodes in the future, PRs are welcomed as always! ).

If `SendInput` doesn't specify a down/up stroke, two commands are generated without any delay.

An additional 5 seconds of sleep is added at the front of the bash script to allow you to alt-tab over to your game.

## Why is it in Java?

I have a java exam tomorrow as of writing this. Need some practice using Swing, Stream processing, and Functional programming before going in.

Perhaps will rewrite the frontend in an actual GUI toolkit instead of Swing in the future if I have time.


## Usage

Either run with `java -jar 'ahktobash-jar-with-dependencies.jar'`

or install as flatpak with `flatpak install --user com.github.ykshek.AhkToBash.flatpak`

Files selected will be copied to `~/output/`, and deleted after converting. Converted files are similarly also in `~/output/`

## Note

~~You may need to manually create an `~/output/` folder to run the program.~~ Should be fixed now, the program will automatically create one for you, and the flatpak permissions are updated accordingly to `~/output:create`

May or may not convert to xdg file standards, however currently I can't think of a way to minimally give flatpak filesystem permissions to create and copy over and convert the large number of bash script files.
