import numpy as np
import matplotlib.pyplot as plt

a = 0
b = 2
c = 0
d = 3

Nx = 20
Ny = 30



A_approximation = []
with open("./output_5_2_approximation.txt", "r") as file:
    line = file.readline()
    while (line):
        A_approximation.append(line.split())
        line = file.readline()

A_analytic = []
with open("./output_5_2_analytic.txt", "r") as file:
    line = file.readline()
    while (line):
        A_analytic.append(line.split())
        line = file.readline()


x = np.linspace(a, b, len(A_analytic))
y = np.linspace(c, d, len(A_analytic[0]))

X, Y = np.meshgrid(x, y)
fig = plt.figure(figsize=(8,6))
ax = fig.add_subplot(projection='3d')
Z_analytic = np.asmatrix(A_analytic)
Z_approximation = np.asmatrix(A_approximation)

print(Z_analytic.T)
print(Z_approximation.T)
ax.plot_surface(X, Y, Z_analytic.T, color="#f00", alpha=1)
ax.plot_surface(X, Y, Z_approximation.T, color='#00f', alpha=0.5)
plt.show()