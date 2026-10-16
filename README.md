# Liriope

A Paper plugin for custom world borders drawn with particles: circle or square, for a whole world or for a single
player.

Requires [Aster](https://github.com/Hana-Development/Aster) installed on the server.

## Installation

Available on [Maven Central](https://central.sonatype.com/artifact/io.github.lotiny/liriope-api). Replace `VERSION` with
the latest release.

### Gradle (Kotlin DSL)

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    compileOnly("io.github.lotiny:liriope-api:VERSION")
}
```

### Gradle (Groovy)

```groovy
repositories {
    mavenCentral()
}

dependencies {
    compileOnly 'io.github.lotiny:liriope-api:VERSION'
}
```

### Maven

```xml
<dependency>
    <groupId>io.github.lotiny</groupId>
    <artifactId>liriope-api</artifactId>
    <version>VERSION</version>
    <scope>provided</scope>
</dependency>
```

### Depend on Liriope

`plugin.yml`:

```yaml
depend: [ Liriope ]
```

or `paper-plugin.yml`:

```yaml
dependencies:
  server:
    Liriope:
      load: BEFORE
      required: true
      join-classpath: true
```

## Usage

```java
import com.cheonsa.liriope.api.ILiriopeAPI;
import com.cheonsa.liriope.api.LiriopeAPI;
import com.cheonsa.liriope.api.border.BorderType;
import com.cheonsa.liriope.api.border.IBorder;
import org.bukkit.Color;

ILiriopeAPI api = LiriopeAPI.getApi(); // null if Liriope is not enabled

// Border for every player in a world
IBorder border = api.getBorder(world);
border.setBorderType(BorderType.SQUARE);
border.setOrigin(0, 0);
border.setRadius(500);           // instantly
border.setRadius(50, 300);       // shrink to 50 over 5 minutes
border.setDamage(2.0);           // one heart
border.setDamageTick(20);        // every second
border.setColor(Color.RED);

// Border for a single player, overrides the world border for them
IBorder personal = api.getBorder(player);
personal.setOrigin(player.getLocation());
personal.setRadius(20);

api.removeBorder(player);        // back to the world border
```

There are two kinds of border:

- **World border** `getBorder(World)`: affects every player in that world.
- **Player border** `getBorder(Player)`: affects only that player, in any world. While it exists the world border is
  ignored for them.

`getBorder` creates the border if missing; use `hasBorder` to only check. New borders start as a copy of the world's
vanilla border.

### Events

```java
@EventHandler
public void onLeave(LiriopePlayerLeaveBorderEvent event) {
    event.getPlayer().sendMessage("You left the border!");
}

@EventHandler
public void onDamage(LiriopePlayerDamageByBorderEvent event) {
    if (event.getPlayer().hasPermission("border.bypass")) {
        event.setCancelled(true);
    }
}
```

| Event                              | When                                                                        | Cancellable |
|------------------------------------|-----------------------------------------------------------------------------|-------------|
| `LiriopePlayerEnterBorderEvent`    | Player goes from outside to inside                                          | No          |
| `LiriopePlayerLeaveBorderEvent`    | Player goes from inside to outside (also when the border shrinks past them) | No          |
| `LiriopePlayerDamageByBorderEvent` | Before a player outside takes border damage                                 | Yes         |

All events have `getBorder()` to tell which border fired.

### Visual settings

| Method            | Default | Recommended |
|-------------------|---------|-------------|
| `setParticleSize` | 1.5     | 1.0 - 2.0   |
| `setViewDistance` | 16      | 8 - 24      |
| `setPointSpacing` | 2       | 1 - 3       |
| `setBandHeight`   | 6       | 3 - 8       |
| `setRowSpacing`   | 2       | 1 - 3       |
| `setDrawInterval` | 8 ticks | 4 - 10      |

Smaller spacing or larger distance/height means more particles per player; halving a spacing doubles them. See the
Javadoc on `IBorder` for details.
