# Solar Flux Reborn
SFR is a mod that adds Solar Panels that generate FE, RF and partially TESLA
The mod comes by default with 8 different tiers of solar panels (fully configurable)

# How to add to your development environment
Include following code into your build.gradle:

>repositories {
>		maven {
>				name = "APengu Maven"
>				url = "http://apengu.github.io/PenguLibs"
>		}
>}
>
>dependencies {
>		deobfCompile "apengu:HammerCore:MCVERSION-HCVERSION:deobf"
>}

Where "MCVERSION" is Minecraft Version and "HCVERSION" is Hammer Core Version starting from 1.9.2
