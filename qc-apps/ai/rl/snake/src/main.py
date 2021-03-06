'''
Basic implementation of Q Learning without neural networks
Sometimes the machine will get stuck in an infinite loop of non-scoring moves. If so just rerun the script
'''
import random
from Snake import SnakeGame
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation
from quantumcat.applications.generator import RandomNumber
from quantumcat.utils import providers
import streamlit as st

labels = []
ims = []
dataArrays = []
scores = []

def evaluateScore(Q, boardDim, numRuns, displayGame=False):
    # Run the game for a specified number of runs given a specific Q matrix
    cutoff = 100  # X moves without increasing score will cut off this game run
    scores = []
    for i in range(numRuns):
        game = SnakeGame(boardDim, boardDim)
        state = game.calcStateNum()
        score = 0
        oldScore = 0
        gameOver = False
        moveCounter = 0
        while not gameOver:
            possibleQs = Q[state, :]
            action = np.argmax(possibleQs)
            state, reward, gameOver, score = game.makeMove(action)
            if score == oldScore:
                moveCounter += 1
            else:
                oldScore = score
                moveCounter = 0
            if moveCounter >= cutoff:
                # stuck going back and forth
                break
        scores.append(score)
    return np.average(scores), scores

def animate(frameNum):
    print(frameNum)
    for i, im in enumerate(ims):
        labels[i].set_text("Length: " + str(scores[i][frameNum]))
        ims[i].set_data(dataArrays[i][frameNum])
    return ims+labels


def train(selected_option):
    # %%
    boardDim = 16  # size of the baord

    # state is as follows.
    # Is direction blocked by wall or snake?
    # Is food in this direction? can either be one or two directions eg (food is left) or (food is left and up)
    # state =  (top blocked, right blocked, down blocked, left blocked, up food, right food, down food, left food)
    # 8 boolean values. Not all states are reachable (eg states with food directions that don't make sense)
    numStates = 2 ** 8
    numActions = 4  # 4 directions that the snake can move
    Q = np.zeros((numStates, numActions))

    # lr = 0.9 #learning rate. not used in this Q learning equation
    gamma = 0.8  # discount rate
    epsilon = 0.2  # exploration rate in training games
    numEpisodes = 10001  # number of games to train for

    Qs = np.zeros([numEpisodes, numStates, numActions])
    bestLength = 0
    rand_uniform_count = 0
    rand_int_count = 0
    for episode in range(numEpisodes):
        game = SnakeGame(boardDim, boardDim)
        state = game.calcStateNum()
        gameOver = False
        score = 0
        while not gameOver:
            rand_uniform = random.uniform(0, 1)
            rand_uniform_count = rand_uniform_count + 1
            if rand_uniform < epsilon:
                DECIMAL = 'decimal'
                if selected_option == 'IBM Qiskit':
                    provider_option = providers.IBM_PROVIDER
                elif selected_option == 'Google Cirq':
                    provider_option = providers.GOOGLE_PROVIDER
                elif selected_option == 'AWS Braket':
                    provider_option = providers.AMAZON_PROVIDER
                quantum_random_num = RandomNumber(length=2, output_type=DECIMAL).\
                    execute(provider=provider_option)
                rand_int_count = rand_int_count + 1
                action = quantum_random_num
            else:
                possibleQs = Q[state, :]
                action = np.argmax(possibleQs)
            new_state, reward, gameOver, score = game.makeMove(action)

            # http: // mnemstudio.org/path-finding-q-learning-tutorial.htm
            Q[state, action] = reward + gamma * np.max(Q[new_state, :])

            # https://towardsdatascience.com/simple-reinforcement-learning-q-learning-fcddc4b6fe56
            # Q[state, action] = Q[state, action] + lr * (reward + gamma * np.max(Q[new_state, :]) - Q[state, action])
            state = new_state
        Qs[episode, :, :] = np.copy(Q)
        if episode % 100 == 0:
            averageLength, lengths = evaluateScore(Q, boardDim, 25)
            if averageLength > bestLength:
                bestLength = averageLength
                bestQ = np.copy(Q)
            # print("Episode", episode, "Average snake length without exploration:", averageLength,
            #       'Number of times quantum random number generated:', rand_int_count)
        if episode % 500 == 0:
            st.write("Episode", episode, 'Quantum random number generated', rand_int_count, 'times')
            rand_uniform_count = 0
            rand_int_count = 0

    # %%
    # Animate games at different episodes
    print("Generating data for animation...")
    # plotEpisodes = [0, 200, 300, 400, 500, 600, 700, 800, 900]
    plotEpisodes = [0, 200, 400, 600, 800, 1000, 2500, 5000, 10000]
    # plotEpisodes = [2500, 5000, 10000]
    fig, axes = plt.subplots(3, 3, figsize=(9, 9))

    print('axes', axes)

    axList = []
    st.write(axes)
    for i, row in enumerate(axes):
        for j, ax in enumerate(row):
            ax.set_title("Episode " + str(plotEpisodes[i * len(row) + j]))
            ax.get_yaxis().set_visible(False)
            ax.get_xaxis().set_visible(False)
            axList.append(ax)
            ims.append(ax.imshow(np.zeros([boardDim, boardDim]), vmin=-1, vmax=1, cmap='RdGy'))
            labels.append(
                ax.text(0, 15, "Length: 0", bbox={'facecolor': 'w', 'alpha': 0.75, 'pad': 1, 'edgecolor': 'white'}))
            dataArrays.append(list())
            scores.append(list())

    stopAnimation = False
    maxFrames = 1000
    cutoff = 100
    numGames = 10
    for k in range(numGames):
        games = []
        states = []
        gameOvers = []
        moveCounters = []
        oldScores = []
        for l in range(len(plotEpisodes)):
            game = SnakeGame(boardDim, boardDim)
            games.append(game)
            states.append(game.calcStateNum())
            gameOvers.append(False)
            moveCounters.append(0)
            oldScores.append(0)
        for j in range(maxFrames):
            for i in range(len(plotEpisodes)):
                possibleQs = Qs[plotEpisodes[i], :, :][states[i], :]
                action = np.argmax(possibleQs)
                states[i], reward, gameOver, score = games[i].makeMove(action)
                if gameOver:
                    gameOvers[i] = True
                dataArrays[i].append(games[i].plottableBoard())
                scores[i].append(score)
                if score == oldScores[i]:
                    moveCounters[i] += 1
                else:
                    oldScores[i] = score
                    moveCounters[i] = 0
                if moveCounters[i] >= cutoff:
                    # stuck going back and forth
                    gameOvers[i] = True
            if not any(gameOver == False for gameOver in gameOvers):
                print("Game", k, "finished, total moves:", len(dataArrays[0]))
                break

    numFrames = len(dataArrays[0])
    ani = animation.FuncAnimation(fig, func=animate, frames=1000, blit=True, interval=75, repeat=False, )
    plt.show(block=False)
    text_placeholder = st.empty()
    text_placeholder.write("Generating Video...")
    ani.save('videos/AnimatedGames.mp4', fps=15, extra_args=['-vcodec', 'libx264'])
    video_file = open('videos/AnimatedGames.mp4', 'rb')
    video_bytes = video_file.read()
    text_placeholder.empty()
    st.video(video_bytes)


if __name__ == '__main__':

    # Set page title and favicon.
    quantumcat_logo_url = 'https://github.com/artificial-brain/quantumcat/raw/assets/quantumcat/logo/quantum_cat_logo.jpg?raw=true'
    st.set_page_config(
        page_title="quantumcat", page_icon=quantumcat_logo_url,
    )

    st.write('## Demo of quantumcat Random Number generator')
    trained_video_file = open('videos/trained_snake.mp4', 'rb')
    trained_video_bytes = trained_video_file.read()
    video_placeholder = st.empty()
    video_placeholder.video(trained_video_bytes)
    select_box = providers.DEFAULT_PROVIDER
    with st.sidebar:
        st.write("## Quantum Provider")

        select_box = st.selectbox('From which quantum provider you want to generate random number?',
                                       ('IBM Qiskit', 'Google Cirq', 'AWS Braket'))
        select_button = st.button('Start Training')

    if select_button:
        st.write('Training started for 10000 Episodes. A video of trained snake would be shown post training.')
        video_placeholder.empty()
        train(select_box)