import random

random.seed(100)


motifs = {
    'oxxxo': 30.0,
    'xxxx': 70.0,
}

def generatePattern(largeur, hauteur):
    res = ""
    for i in range(largeur):
        noeud = 0
        for j in range(hauteur):
            picked = random.choices(*zip(*motifs.items()))[0]
            for lettre in picked:
                if lettre == "x":
                    res += str(noeud) + " " + str(noeud + 1) + " " + str(i) + "\n"
                    noeud += 2
                else:
                    noeud += 1
    return res

def generatePattern2(largeur, hauteur, pattern):
    res = ""
    for i in range(largeur):
        noeud = 0
        for j in range(hauteur):
            for lettre in pattern[i % len(pattern)]:
                if lettre == "x":
                    res += str(noeud) + " " + str(noeud + 1) + " " + str(i) + "\n"
                    noeud += 2
                else:
                    noeud += 1
    return res


"""
for i in range(200):
    file = open("worstCaseBinhHauteur" + str(500 * i) + ".txt", "w")
    file.write(generatePattern(100 * i, 3))
    file.close()

print(generatePattern2(
    largeur=6,
    hauteur=2,
    pattern=pattern))
"""

pattern = [
    "xxxx",
    "xxxx",
    "oxxxo"
]

# HAUTEUR

"""
for i in range(100):
    file = open("worstCaseBinhHauteur" + str(10 * i * 8) + ".txt", "w")
    file.write(generatePattern2(
        largeur=3,
        hauteur=10 * i * 8,
        pattern=pattern
    ))
    file.close()
"""

# LARGEUR

for i in range(100):
    file = open("worstCaseBinhLargeur" + str(100 * i * 3) + ".txt", "w")
    file.write(generatePattern2(
        largeur=100 * i * 3,
        hauteur=8,
        pattern=pattern
    ))
    file.close()