

# Mindustry Java Utility Mod (OmniCorp / Other Servers)

A Java-based Mindustry mod designed primarily for desktop (PC), with optional Android support via GitHub Actions.

---

## 🛠 Building for Desktop

1. Install **JDK 17**.
2. Run the following command:
   ./gradlew jar
3. The compiled `.jar` file will be located in the `build/libs` directory.

⚠️ This build is **not compatible with Android**.
To build for Android, you must install the Android SDK manually or use the provided GitHub Actions workflow.

---

## ⚙️ Features

### 🔁 Teamswitching Utility

**Note:** This works **only** on servers that support the advanced `/team` command (e.g., OmniCorp sandbox servers).

**How to use:**

* Start the switching loop:
  .start teamswitching

* Every 5 seconds, the mod will send a random team command like:
  /team \<random 0-255>

* To stop:
  .stop teamswitching

---

Feel free to fork, modify, or contribute!
