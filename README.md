# Pwned Password Offline

This is another implementation of using the HaveIBeenPwned password hash file to check if your password has been compromised. The big difference between this and others I've seen is that I attempted to make a GUI wrapper around the actual look-up code. I wanted something I could set up and let non-technical people test their passwords. 

For more information on what the password hash file even is check this blog post: [https://www.troyhunt.com/introducing-306-million-freely-downloadable-pwned-passwords/](https://www.troyhunt.com/introducing-306-million-freely-downloadable-pwned-passwords/)

### Build Instructions

1. Download the SHA-1 file (orderered by hash) from https://haveibeenpwned.com/Passwords. This file you download is a 12 GB 7zip file which contains a 25GB txt file.

2. Clone this repository. You'll need the Java JDK to build the java GUI component using Maven and Python installed to run the search file. 

3. Use ```mvn install``` to build the zip file or download a release file from the [Releases](https://github.com/eau-claire-energy-cooperative/pwned-password-offline-gui/releases) area.

3. Take the zipped file (downloaded or generated) and extract it where you want to run the program.  

4. Move the `pwned-passwords-sha1-ordered-by-hash-v4.txt` to the python directory within this folder. 

5. Run the executable JAR file. 

### Usage

Pretty simple, type your passwords in the type text box and hit "Start Search". This will encode the passwords with SHA-1 and kick-start the python process. Results will be displayed in the lower text window. 

Nothing during this process is saved to the file system. The passwords are encoded and passed via command line arguments to the python process.

### Sources

Python file is a modified versio of the original [https://github.com/pinae/HaveIBeenPwnedOffline](https://github.com/pinae/HaveIBeenPwnedOffline)

Password database is compiled and maintained by [Troy Hunt](https://haveibeenpwned.com/)
