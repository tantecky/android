import numpy as np
from matplotlib import pyplot as plt


GRID_SIZE = 8
CELL_WIDTH = 1.0 / GRID_SIZE
CELL_HEIGHT = 1.0 / GRID_SIZE
DT = 0.01
T_BC = 0.0
DIFFUSIVITY = 0.01


def solve_timestep(grid):
    grid_new = np.zeros((GRID_SIZE, GRID_SIZE))
    grid_new[0, :] = T_BC
    grid_new[-1, :] = T_BC
    grid_new[:, 0] = T_BC
    grid_new[:, -1] = T_BC

    grid_new[1:-1, 1:-1] = (
        grid[1:-1, 1:-1]
        + DIFFUSIVITY
        * DT
        / CELL_WIDTH ** 2
        * (grid[1:-1, 2:] - 2 * grid[1:-1, 1:-1] + grid[1:-1, 0:-2])
        + DIFFUSIVITY
        * DT
        / CELL_HEIGHT ** 2
        * (grid[2:, 1:-1] - 2 * grid[1:-1, 1:-1] + grid[0:-2, 1:-1])
    )

    return grid_new


def main():
    grid = np.zeros((GRID_SIZE, GRID_SIZE))
    grid[GRID_SIZE // 2, GRID_SIZE // 2] = 1

    cfl = DIFFUSIVITY * DT / CELL_WIDTH / CELL_HEIGHT
    print(f"CFL: {cfl}")

    plt.figure()
    plt.imshow(grid, interpolation="nearest", vmin=0, vmax=1, cmap="jet")

    for _ in range(3):
        grid = solve_timestep(grid)
        plt.figure()
        plt.imshow(grid, interpolation="nearest", vmin=0, vmax=1, cmap="jet")
        plt.colorbar()

    plt.show()


if __name__ == "__main__":
    main()
