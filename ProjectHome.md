# Trace Viewer IntelliJ Plugin #

## Download ##
  * Go to the 'Downloads' tab and [download the latest release jar file](http://traceviewerplugin.googlecode.com/files/TraceViewerPlugin.jar)
  * Copy it in your IntelliJ plugin directory.

## Overview ##
  * Display traces files in a console window
  * Tab management
  * Trace highligthing
  * Resume or Filter actions
  * Exceptions links

![http://cgonguet.free.fr/TraceViewerPlugin/images/overview.jpg](http://cgonguet.free.fr/TraceViewerPlugin/images/overview.jpg)

## Setup ##

  * Go to 'Settings > IDE Settings > Trace Viewer'
> > ![http://cgonguet.free.fr/TraceViewerPlugin/images/settings_icon.jpg](http://cgonguet.free.fr/TraceViewerPlugin/images/settings_icon.jpg)

  * Configure your default trace files repository and your default file name
> > ![http://cgonguet.free.fr/TraceViewerPlugin/images/setup_1.jpg](http://cgonguet.free.fr/TraceViewerPlugin/images/setup_1.jpg)

  * Configure the trace patterns and the highligths:
    * Trace regexp: the regular expression defining a trace (prefix common to all traces)
    * By block: manage the trace by block (until a new trace begins) or line by line
    * Regexp: regular expression matching the trace
    * Style: color and font for this trace
    * Resume: this trace should be present or not in the resume
    * Filter: this trace should be present or not in the filtered version
> > ![http://cgonguet.free.fr/TraceViewerPlugin/images/setup_2.jpg](http://cgonguet.free.fr/TraceViewerPlugin/images/setup_2.jpg)

## Launch ##

  * Click 'Tools > Trace Viewer' in menu.
  * Choose the trace file.
> > ![http://cgonguet.free.fr/TraceViewerPlugin/images/menu.jpg](http://cgonguet.free.fr/TraceViewerPlugin/images/menu.jpg)

  * The trace file is opened in the console window
> > ![http://cgonguet.free.fr/TraceViewerPlugin/images/console.jpg](http://cgonguet.free.fr/TraceViewerPlugin/images/console.jpg)