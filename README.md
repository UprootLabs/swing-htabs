swing-htabs [![Gitter](https://badges.gitter.im/UprootLabs/swing-htabs.png)](https://gitter.im/UprootLabs/swing-htabs?utm_source=badge) [![Build Status](https://travis-ci.org/UprootLabs/swing-htabs.svg?branch=master)](https://travis-ci.org/UprootLabs/swing-htabs)
===========

Hierarchical tabs for Swing.

![Screenshot of swing-htabs](http://i.imgur.com/V36Bjf6.gif)

### Background
The Java Swing UI doesn't have support for creating hierarchical tabs (tabs that are shown as a tree).
We needed this for [gngr](https://gngr.info).
We couldn't find any existing libraries to achieve this, hence this project.

### Status
The current status of this library is a *proof-of-concept*. We are using a little hack to indent the tab components
in a custom JTabbedPane. It works fine in our tests, but we need more testing. Please see the **contributing** section below.

The Metal LAF has a small, known issue. The tabs in this LAF have a little notch that overlaps with the next tab, making
it difficult to indent without looking odd. As a workaround we are indenting only the tab content.

The API will be stabilized after testing.

### Contributing
We need more testing to complete this matrix:

JRE v/s LAF                      | Metal    | GTK+     | Nimbus   | Motif   | Windows | Windows Classic | MacOSX
-----------------                | :-----:  | :-----:  | :-----:  | :-----: | :-----: | :-------------: | :-----: 
Ubuntu14.x + OracleJRE8          | ☑        | ☑        | ☑        | ☑       | -       | -               | -       
Ubuntu14.10 + OpenJDK7           | ☑        | ☑        | ☑        | ☑       | -       | -               | -       
Windows XP + Oracle JVM 7        | ☑        | -        | ☑        | ☑       | ☑       | ☑               | -       
MacOSX + OracleJRE8              | ☑        | -        | ☑        | ☑       | -       | -               | ☑       
Ubuntu14.04+tilingWindowMgr+OracleJVM7| ☑        | ☑[issue](https://github.com/UprootLabs/swing-htabs/issues/22)  | ☑        | ☑       | -       | -               | -       
`TODO`: More Windows versions    | -        | -        | -        | -       | -       | -               | -

#### How to test
Testing is easy; download the latest jar from the `releases` page. After running the jar file, you will
see some randomly generated tabs, with the System LAF. Try switching to other LAFs using the radio buttons
on the top. Append your report to [this issue](https://github.com/UprootLabs/swing-htabs/issues/1)

## Copyright and License

Copyright 2014 Uproot Labs India

Distributed under the [Apache v2 License](https://www.apache.org/licenses/LICENSE-2.0.html)
