# GrafMasterJava

GrafMasterJava is a small Java/Swing desktop app for loading, visualizing, splitting, and saving graphs. It ships with a GUI that can read graph files from the `data/` folder, display them, and partition a graph into balanced subgraphs.

## Features

- Load graphs from text (`.txt`) or binary (`.bin`) files.
- Visualize one or multiple graphs in a grid layout.
- Split a graph into _n_ balanced parts with a maximum difference threshold.
- Save the resulting subgraphs back to `.txt` and/or `.bin`.
- Zoom and pan inside the graph view (Ctrl + mouse wheel to zoom, drag to pan, double‑click to reset).

## Requirements

- Java 17+ (or any recent Java version that can run Swing)
- No external dependencies

## Running the app

### Using the command line

From the repo root:

```bash
javac -d out src/*.java
java -cp out Main
```

### Using an IDE

Open the project in IntelliJ IDEA (or another Java IDE) and run the `Main` class.

## Usage

1. Place your graph files in the `data/` directory.
2. Start the application.
3. Select a file from the list and click **Wczytaj Graf** (Load Graph).
4. Use:
   - **Rysuj Graf** to render the graph(s),
   - **Podziel Graf** to split the graph into parts,
   - **Force** to force a split into 2 parts,
   - **Zapisz Graf** to save output graphs.
5. The status box shows validation errors and success messages.

## File formats

### Text format (`.txt`)

The text file can contain multiple graphs, separated by a header line that starts with `#`.
Each vertex line has the following structure:

```
<id>: <neighbor1> <neighbor2> ... (<x>,<y>)
```

Example:

```
#1
0: 1 2 (10,20)
1: 0 2 (40,10)
2: 0 1 (25,35)
```

### Binary format (`.bin`)

Binary files start with the signature `SUBG` and version `1`. The format is little‑endian.
The layout is:

- `SUBG` (4 bytes)
- version (1 byte)
- number of parts (2 bytes, unsigned)
- for each part:
  - number of vertices (2 bytes, unsigned)
  - for each vertex:
    - id (2 bytes, unsigned)
    - x (2 bytes, signed)
    - y (2 bytes, signed)
    - degree (2 bytes, unsigned)
    - edges: `degree` × 2 bytes (unsigned)

## Project structure

```
src/
  Main.java             # Application entry point
  Gui.java              # Swing GUI and rendering
  GraphLoader.java      # Loading graphs from .txt/.bin
  GraphSaver.java       # Saving graphs to .txt/.bin
  GraphPartitioner.java # Graph splitting logic
  Validator.java        # Graph validation helpers
  GraphChunk.java       # Graph container
  Vertex.java           # Vertex model
```

## Notes

- The app reads and writes files inside the `data/` directory by default.
- If you split a graph and close the app without saving, a confirmation prompt is shown.
