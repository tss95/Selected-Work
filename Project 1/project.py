import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split

dataset = pd.read_csv('abalone.data')
columnHeaders = dataset.columns

data = dataset.values

X = dataset.values[:, 0:8]
y = dataset.values[:, -1]

X_train, X_val, y_train, y_val = train_test_split(X,y, test_size = 0.8)

# Returns true if there is only one value of y in the input, false otherwise
def isPure(y):
    if len(set(y)) == 1:
        return True
    else:
        return False

# Returns a list indexed by each column in X providing its category
def typeOfFeature(data, threshold=5):
    featureTypes = []
    X = data[:, 0:8]
    y = data[:, 8]
    _, nColumns = X.shape
    X_t = X.T

    for column in range(nColumns):
        uniqueValues = np.unique(X_t[column])
        example = uniqueValues[0]

        if isinstance(example, str) or len(uniqueValues) <= threshold:
            featureTypes.append("categorical")
        else:
            featureTypes.append("continious")

    return featureTypes


featureTypes = typeOfFeature(np.column_stack((X_train, y_train)))

# Returns the type of column based on a threshold (by default 5).
def typeOfColumn(col, threshold = 5):
    uniqueValues = list(set(col))
    example = uniqueValues[0]
    if isinstance(example,str) or len(uniqueValues) <= threshold:
        featureType = "categorical"
    else:
        featureType = "continious"
    return featureType

# Returns the most frequent class
def classify(y):
    classes, counts = np.unique(y,return_counts=True)
    indexOfMostFrequentClass = counts.argmax()
    return classes[indexOfMostFrequentClass]

# Finds the 'ideal' split of each column. For continious data it uses the mean
# and for categorical data it outputs each category as a potential split
def getPotentialSplits(data):
    potentialSplits = []
    X = data[:, 0:8]
    y = data[:, 8]
    _, nColumns = X.shape

    types = typeOfFeature(data)

    for column in range(nColumns):
        values = X[:, column]

        if types[column] == "continious":
            potentialSplits.append(sum(values) / len(y))

        else:
            uniqueValues = np.unique(values)
            potentialSplits.append(uniqueValues)

    return potentialSplits

# Identifies the index of the column based in its column name
def columnIdentifier(columnName):
    if columnName == 'Sex':
        return 0
    elif columnName == 'Length':
        return 1
    elif columnName == 'Diameter':
        return 2
    elif columnName == 'Height':
        return 3
    elif columnName == 'Whole_Weight':
        return 4
    elif columnName == 'Shucked_Weight':
        return 5
    elif columnName == 'Viscera_Weight':
        return 6
    elif columnName == 'Shell_Weight':
        return 7
    elif columnName == 'Rings':
        return 8


# Splits the data into 2 datasets based on the split value and column.
def splitData(data, splitColumn, splitValue):
    splitColumnValues = data[:, splitColumn]
    dataType = typeOfFeature(data)
    if dataType[splitColumn] == 'continious':
        dataBelow = data[splitColumnValues <= splitValue]
        dataAbove = data[splitColumnValues > splitValue]
    else:
        dataBelow = data[splitColumnValues == splitValue]
        dataAbove = data[splitColumnValues != splitValue]

    return dataBelow, dataAbove


# Calculates the entropy of the entire inputted dataset.
def getEntropy(y):
    _, counts = np.unique(y, return_counts=True)

    probabilities = counts / counts.sum()
    entropy = sum(probabilities * -np.log2(probabilities))

    return entropy

# Calculates the 'conditional' entropy of the two datasets.
def getOverallEntropy(dataBelow, dataAbove):
    nPoints = len(dataBelow) + len(dataAbove)

    pBelow = len(dataBelow) / nPoints
    pAbove = len(dataAbove) / nPoints

    overallEntropy = (pBelow * getEntropy(dataBelow[:, 8])) + (
                pAbove * getEntropy(dataAbove[:, 8]))

    return overallEntropy

# Calculates the gini index of the inputed dataset
def getGini(data):
    y = data[:, 8]
    classes, counts = np.unique(y, return_counts=True)
    probabilties = counts / counts.sum()
    gini = 1 - sum(probabilties ** 2)
    return gini


#getGini_data(data)


# Calculates the 'conditional' gini index.
def getOverallGini(dataBelow, dataAbove):
    nPoints = len(dataBelow) + len(dataAbove)
    giniBelow = getGini(dataBelow)
    giniAbove = getGini(dataAbove)

    pBelow = len(dataBelow) / nPoints
    pAbove = len(dataAbove) / nPoints

    overallGini = pBelow * giniBelow + pAbove * giniAbove

    return overallGini

# dataBelow, dataAbove = splitData(data, 7, 0.2355604)
# getOverallGini(dataBelow, dataAbove)

# Determines the best possible split of the columns for the tree. Supports both
# entropy and gini.
def determineOptimalSplit(data, potentialSplits, impurity_measure='entropy'):
    X = data[:, 0:8]
    y = data[:, 8]
    X_t = X.T
    _, nColumns = X.shape
    featureTypes = typeOfFeature(data)

    if impurity_measure == 'entropy':
        overallEntropy = 1000
        for index in range(nColumns):
            if featureTypes[index] == "continious":
                dataBelow, dataAbove = splitData(data, index,
                                                 potentialSplits[index])
                currentOverallEntropy = getOverallEntropy(dataBelow,
                                                          dataAbove)

                if currentOverallEntropy < overallEntropy:
                    overallEntropy = currentOverallEntropy
                    bestSplitColumn = index
                    bestSplitValue = potentialSplits[index]
            else:
                for categoricalIndex in range(len(potentialSplits[index])):
                    dataBelow, dataAbove = splitData(data, index,
                                                     potentialSplits[index][
                                                         categoricalIndex])
                    currentOverallEntropy = getOverallEntropy(dataBelow,
                                                              dataAbove)

                    if currentOverallEntropy < overallEntropy:
                        overallEntropy = currentOverallEntropy
                        bestSplitColumn = index
                        bestSplitValue = potentialSplits[index][
                            categoricalIndex]

    if impurity_measure == 'gini':
        overallGini = getGini(data)
        for index in range(nColumns):
            if featureTypes[index] == "continious":
                dataBelow, dataAbove = splitData(data, index,
                                                 potentialSplits[index])
                currentOverallGini = getOverallGini(dataBelow, dataAbove)

                if currentOverallGini < overallGini:
                    overallGini = currentOverallGini
                    bestSplitColumn = index
                    bestSplitValue = potentialSplits[index]
            else:
                for categoricalIndex in range(len(potentialSplits[index])):
                    dataBelow, dataAbove = splitData(data, index,
                                                     potentialSplits[index][
                                                         categoricalIndex])
                    currentOverallGini = getOverallGini(dataBelow,
                                                        dataAbove)

                    if currentOverallGini < overallGini:
                        overallGini = currentOverallGini
                        bestSplitColumn = index
                        bestSplitValue = potentialSplits[index][
                            categoricalIndex]

    return bestSplitColumn, bestSplitValue


#potentialSplits = getPotentialSplits(np.column_stack((X_train, y_train)))
#determineOptimalSplit(np.column_stack((X_val, y_val)), potentialSplits, 'gini')

#columnIdentifier('Shucked_Weight')

# Creates a decision tree based on the training data. Supports both gini and
# entropy.

def Learn(X, y, impurity_measure='entropy'):
    data = np.column_stack((X, y))

    if isPure(y):
        classification = classify(y)
        return classification


    else:

        potentialSplits = getPotentialSplits(data)
        if impurity_measure == 'entropy':
            splitColumn, splitValue = determineOptimalSplit(data,
                                                            potentialSplits,
                                                            'entropy')
        elif impurity_measure == 'gini':
            splitColumn, splitValue = determineOptimalSplit(data,
                                                            potentialSplits,
                                                            'gini')
        dataBelow, dataAbove = splitData(data, splitColumn, splitValue)

        featureName = columnHeaders[splitColumn]
        featureType = featureTypes[splitColumn]
        if featureType == 'categorical':
            question = "{} == {}".format(featureName, splitValue)
        else:
            question = "{} <= {}".format(featureName, splitValue)
        subTree = {question: []}

        if impurity_measure == 'entropy':
            yesAnswer = Learn(dataBelow[:, 0:8], dataBelow[:, 8], 'entropy')
            noAnswer = Learn(dataAbove[:, 0:8], dataAbove[:, 8], 'entropy')
        elif impurity_measure == 'gini':
            yesAnswer = Learn(dataBelow[:, 0:8], dataBelow[:, 8], 'gini')
            noAnswer = Learn(dataAbove[:, 0:8], dataAbove[:, 8], 'gini')

        subTree[question].append(yesAnswer)
        subTree[question].append(noAnswer)

        return subTree



# Predicts a single input based on the tree.
def predict(x, tree):
    question = list(tree.keys())[0]
    columnName, operator, value = question.split()
    columnName = columnIdentifier(columnName)
    if operator == '==':
        if x[columnName] == value:
            response = tree[question][0]
        else:
            response = tree[question][1]
    else:
        if x[columnName] <= float(value):
            response = tree[question][0]
        else:
            response = tree[question][1]
    if not isinstance(response, dict):
        return response
    else:
        remaining_tree = response
        return predict(x, remaining_tree)

# Calculates the accuracy of the tree using the validation data.
def getAccuracy(X_val, y_val, tree):
    predictedY = []
    realY = y_val

    for index in range(len(y_val)):
        prediction = predict(X_val[index], tree)
        predictedY.append(prediction)

    mistakes = 0
    for index in range(len(predictedY)):
        if predictedY[index] != realY[index]:
            mistakes += 1

    accuracy = 1 - (mistakes / len(realY))

    return accuracy


from sklearn.tree import DecisionTreeClassifier
from sklearn.metrics import accuracy_score


# Changes the categorical data of the first column into [0,1,3]
def changeX(X):
    X = X[:, 0:8]
    X_t = X.T

    for i in range(len(X_t)):
        if typeOfColumn(X[:, i]) == 'categorical':

            for j in range(len(X_t[i])):
                if X_t[i][j] == 'M':
                    X_t[i][j] = 0
                elif X_t[i][j] == 'F':
                    X_t[i][j] = 1
                elif X_t[i][j] == 'I':
                    X_t[i][j] = 2
        return X


Xchanged = changeX(X)
y = y.astype('int')

X_train2, X_val2, y_train2, y_val2 = train_test_split(Xchanged, y)

clf_gini = DecisionTreeClassifier(criterion='gini', random_state=0)
clf_tree_gini = clf_gini.fit(X_train2, y_train2)
y_pred_gini = clf_gini.predict(X_val2)

clf_entropy = DecisionTreeClassifier(criterion='entropy', random_state=0)
clf_tree_entropy = clf_entropy.fit(X_train2, y_train2)
y_pred_entropy = clf_entropy.predict(X_val2)


tree_entropy = Learn(X_train, y_train, 'entropy')
tree_gini = Learn(X_train, y_train, 'gini')
print("Entropy: " + str(tree_entropy))
print("Gini: " + str(tree_gini))
print("My own tree accuracy (entropy): " + str(getAccuracy(X_val, y_val, tree_entropy)) + "%")
print("My own tree accuracy (gini): " + str(getAccuracy(X_val, y_val, tree_gini)) + "%")
print("skLearn accuracy (entropy):" + str(accuracy_score(y_val2, y_pred_entropy)) + "%")
print("skLearn accuracy (gini):" + str(accuracy_score(y_val2, y_pred_gini)) + "%")

