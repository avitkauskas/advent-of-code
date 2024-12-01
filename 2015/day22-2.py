from collections import deque
import copy

spells = {"Missile": {"cost": 53, "damage": 4},
          "Drain": {"cost": 73, "damage": 2, "heal": 2},
          "Shield": {"cost": 113, "time": 6, "armor": 7},
          "Poison": {"cost": 173, "time": 6, "damage": 3},
          "Recharge": {"cost": 229, "time": 5, "mana": 101}}

state = {"player": {"points": 50, "mana": 500, "armor": 0},
         "boss": {"points": 51, "damage": 9},
         "effects": {"Shield": 0, "Poison": 0, "Recharge": 0},
         "mana_spent": 0}

def possible_spells(state):
    ps = {k: copy.deepcopy(v) for k, v in spells.items() if v["cost"] <= state["player"]["mana"]}
    if state["effects"]["Shield"] > 0 and "Shield" in ps:
        del ps["Shield"]
    if state["effects"]["Poison"] > 0 and "Poison" in ps:
        del ps["Poison"]
    if state["effects"]["Recharge"] > 0 and "Recharge" in ps:
        del ps["Recharge"]
    return ps

Q = deque([copy.deepcopy(state)])
min_mana = 999999999

while Q:
    curr_state = Q.popleft()

    # player loose 1 point in hard level
    curr_state["player"]["points"] -= 1
    if curr_state["player"]["points"] <= 0:
        continue

    # apply effects
    if curr_state["effects"]["Shield"] > 0:
        curr_state["player"]["armor"] = spells["Shield"]["armor"]
        curr_state["effects"]["Shield"] -= 1
    else:
        curr_state["player"]["armor"] = 0
    if curr_state["effects"]["Poison"] > 0:
        curr_state["boss"]["points"] -= spells["Poison"]["damage"]
        curr_state["effects"]["Poison"] -= 1
        if curr_state["boss"]["points"] <= 0:
            min_mana = min(min_mana, curr_state["mana_spent"])
            continue
    if curr_state["effects"]["Recharge"] > 0:
        curr_state["player"]["mana"] += spells["Recharge"]["mana"]
        curr_state["effects"]["Recharge"] -= 1
    
    # get possible spells
    poss_spells = possible_spells(curr_state)
    if not poss_spells:
        continue
    for name, spell in poss_spells.items():
        # player move
        new_state = copy.deepcopy(curr_state)
        new_state["player"]["mana"] -= spell["cost"]
        new_state["mana_spent"] += spell["cost"]
        match name:
            case "Missile":
                new_state["boss"]["points"] -= spell["damage"]
                if new_state["boss"]["points"] <= 0:
                    min_mana = min(min_mana, new_state["mana_spent"])
                    continue
            case "Drain":
                new_state["boss"]["points"] -= spell["damage"]
                new_state["player"]["points"] += spell["heal"]
                if new_state["boss"]["points"] <= 0:
                    min_mana = min(min_mana, new_state["mana_spent"])
                    continue
            case "Shield":
                new_state["effects"]["Shield"] = spell["time"]
            case "Poison":
                new_state["effects"]["Poison"] = spell["time"]
            case "Recharge":
                new_state["effects"]["Recharge"] = spell["time"]

        # boss move
        # player loose 1 point in hard level
        new_state["player"]["points"] -= 1
        if new_state["player"]["points"] <= 0:
            continue

        # apply effects
        if new_state["effects"]["Shield"] > 0:
            new_state["player"]["armor"] = spells["Shield"]["armor"]
            new_state["effects"]["Shield"] -= 1
        else:
            new_state["player"]["armor"] = 0
        if new_state["effects"]["Poison"] > 0:
            new_state["boss"]["points"] -= spells["Poison"]["damage"]
            new_state["effects"]["Poison"] -= 1
            if new_state["boss"]["points"] <= 0:
                min_mana = min(min_mana, new_state["mana_spent"])
                continue
        if new_state["effects"]["Recharge"] > 0:
            new_state["player"]["mana"] += spells["Recharge"]["mana"]
            new_state["effects"]["Recharge"] -= 1
    
        new_state["player"]["points"] -= max(new_state["boss"]["damage"] - new_state["player"]["armor"], 1)
        if new_state["player"]["points"] <= 0:
            continue
        # add new state to queue
        if new_state["mana_spent"] < min_mana:
            Q.append(copy.deepcopy(new_state))
    
print(min_mana)
