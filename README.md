# PersoKits

# Commands

## Kit command
### (/kit)
It will display all aviable kits for player. They need permission "persokit.kit.<name>" for kit or "persokit.kit.*" for all kits.

#### Kit colors:
*can not be configurate now*

**Green** - aviable

**Strikethrough** - kit has no items

**Red** - no usses left for kit

**Yellow** - kit is on cooldown

**Bold** - is PersoKit (player can make own variant for kit)

**Underline** - player have not set own PersoKit yet

### (/kit \<name\>)
If kit is Persokit player will recive own variant (if is set). 

If is not, player will be asked to set own variant or use default.
If kit is regural kit player will only revice kit.

## PersoKit command
### (/pkit)
Will display menu with all kits aviable to set own variant for player (permission based).

### (/pkit \<name\>)
Will display editor to chose own items for kit variant.


## KitEditor command
### (/kiteditor)
Will display menu with all kits to edit.

### (/kiteditor \<name\>
Will display menu with specific kit to edit.


# Permissions:
**""persokit.kit.\<name\>"** - permission to get kit 

**""persokit.kit.*"** - permission to get all kits (players with op will have this permission)

**"persokit.pkit.\<name\>"** - permission to set own variant of kit

**"persokit.pkit.*"** - permission to set own variant of kit for all kits (players with op will have this permission)

**""persokit.editor"** - permission to edit kits (players with op will have this permission)

# TODO:
- Make on join kit.

- Make hologram selection for PersoKit

- Make option to remove/add or rename kit in KitsEditor.

- Add MySQL support.
