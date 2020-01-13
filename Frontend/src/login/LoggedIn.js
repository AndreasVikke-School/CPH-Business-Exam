import React, { useState, useEffect } from 'react';
import facade from './ApiFacade'
import { Tab, Tabs, Nav, Col, Row, Form, Table, InputGroup, FormControl, Alert, Modal, Button, ListGroup } from 'react-bootstrap'

export default function LoggedIn() {
  const [user, setUser] = useState({});
  const [recipes, setRecipes] = useState([]);
  const [week, setWeek] = useState({week: 1});
  const [messages, setMessages] = useState({});

  useEffect(() => {
    facade.fetchUser().then(res => setUser(res)).catch(e => console.log(e));
    facade.fetchAllRecipies().then(res => 
      setRecipes(res.sort(function(a, b) {
        if (a.id.toUpperCase() < b.id.toUpperCase()) return -1;
        if (a.id.toUpperCase() > b.id.toUpperCase()) return 1;
        return 0;
      }))).catch(e => console.log(e));
      if(user.userName !== undefined) {
        facade.fetchMenuPlanByWeek(1, user.userName).then(res => {
            let editWeek = {week: res.week};
            editWeek = {...editWeek, id: res.id};
            res.dayPlans.forEach(dayPlan => {
              editWeek[dayPlan.dayOfWeek] = dayPlan.recipeDTO.id;
            });
            setWeek(editWeek);
        });
      }
  }, [user.userName])

  const onChange = (evt) => {
    let key = evt.target.dataset.key;
    let value = evt.target.value;

    if(key === "week") {
      facade.fetchMenuPlanByWeek(value, user.userName).then(res => {
        let editWeek = {week: res.week};
        editWeek = {...editWeek, id: res.id};
        res.dayPlans.forEach(dayPlan => {
          editWeek[dayPlan.dayOfWeek] = dayPlan.recipeDTO.id;
        });
        setWeek(editWeek);
      }).catch(e => setWeek({ [key]: value}));
    } else {
      setWeek({ ...week, [key]: value});
    }
  }

  const saveWeek = () => {
    let emptyDays = [];
    for(let i = 1; i <= 7; i++)
      if(!Object.keys(week).includes(i.toString()))
        emptyDays.push(i);
    
    if(emptyDays.length > 0)
      setMessages({error: "Day(s) " + emptyDays.join(", ") + " has not been filled"});

    if(emptyDays.length === 0) {
      setMessages({});
      let dayPlans = [];
      for(let i = 1; i <= 7; i++)
        dayPlans.push({
          dayOfWeek: i,
          recipeDTO: {
            id: week[i].trim()
          }
        });

      let menuPlan = {
        week: week.week,
        user: {
          userName: user.userName
        },
        dayPlans: dayPlans
      };
      if(week.id === undefined) {
        facade.createMenuPlan(menuPlan).then(res => {setMessages({success: "Week saved succesfully"}); setWeek({...week, id: res.id})});
      } else {
        facade.editMenuPlan(menuPlan).then(res => {setMessages({success: "Week saved succesfully"});});
      }
    }
  }

  return (
    <div>
      <h3>Username: {user.userName}</h3>
      {messages.error !== undefined ? <Alert variant="danger">{messages.error}</Alert> : ""}
      {messages.success !== undefined ? <Alert variant="success">{messages.success}</Alert> : ""}
      <WeekView recipes={recipes} onChange={onChange} week={week} saveWeek={saveWeek} />
    </div>
  )
}

function WeekView({ recipes, onChange, week, saveWeek }) {
  const createTabLinks = () => {
    let tabs = [];

    for(let i = 1; i <= 7; i++) {
      tabs.push(
        <Nav.Item key={i}>
          <Nav.Link eventKey={i}>Day {i}</Nav.Link>
        </Nav.Item>
      )
    }
    tabs.push(
      <Nav.Item key={8}>
        <Nav.Link eventKey="shoppinglist">Shopping List</Nav.Link>
      </Nav.Item>
    )
    tabs.push(
      <Nav.Item key={9}>
        <Nav.Link eventKey="weekoverview">Week Overview</Nav.Link>
      </Nav.Item>
    )

    return tabs;
  }

  const createTabContent = () => {
    let tabs = [];

    for(let i = 1; i <= 7; i++) {
      tabs.push(
        <Tab.Pane eventKey={i} key={i}>
          <DayView day={i} recipes={recipes} onChange={onChange} week={week} />
        </Tab.Pane>
      )
    }
    tabs.push(
      <Tab.Pane key={8} eventKey="shoppinglist">
        <ShoppingList week={week} recipes={recipes} />
      </Tab.Pane>
    )
    tabs.push(
      <Tab.Pane key={9} eventKey="weekoverview">
        <WeekOverview week={week} />
      </Tab.Pane>
    )

    return tabs;
  }

  return (
    <div className="container">
      <InputGroup size="sm" className="mb-3">
        <InputGroup.Prepend>
          <InputGroup.Text>Week</InputGroup.Text>
        </InputGroup.Prepend>
        <FormControl type="number" data-key="week" value={week.week} onChange={onChange} />
      </InputGroup>
      <Tab.Container defaultActiveKey="1">
        <Row>
          <Col sm={3}>
            <Nav variant="pills" className="flex-column">
              {createTabLinks()}
            </Nav>
          </Col>
          <Col sm={9}>
            <Tab.Content>
              {createTabContent()}
            </Tab.Content>
          </Col>
        </Row>
      </Tab.Container>
      <button className="btn btn-dark col-sm-12" onClick={() => saveWeek()}>Save Week</button>
    </div>
  )
}

function DayView({ day, recipes, onChange, week }) {
  const [recipe, setRecipe] = useState({});

  const createTableData = () => {
    let data = [];

    recipes.forEach((recipe, index) => {
      data.push(
        <tr key={index} style={week[day] === recipe.id ? {"backgroundColor": "#007bff"} : {}} onClick={() => setRecipe(recipe)} >
          <td>
            <Form.Check
              data-key={day}
              type="radio"
              name="recipe"
              id="Test"
              value={recipe.id}
              checked={week[day] === recipe.id}
              onChange={onChange}
            />
          </td>
          <td>{recipe.id}</td>
          <td>{recipe.prep_time}</td>
        </tr>
      )
    });

    return data;
  }

  const closeModal = () => {
    setRecipe({});
  }

  return (
    <div>
      {recipe.id !== undefined ? <RecipeDetails recipe={recipe} close={closeModal} /> : ""}
      <Form>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Selected</th>
              <th>Name</th>
              <th>Preperation time</th>
            </tr>
          </thead>
          <tbody>
            {createTableData()}
          </tbody>
        </Table>
      </Form>
    </div>
  )
}

function RecipeDetails({ recipe, close }) {
  return (
    <div className="recipeModal" onClick={() => close()}>
      <Modal.Dialog>
        <Modal.Header closeButton>
          <Modal.Title>{recipe.id} - {recipe.prep_time}</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <Row>
            <Col sm={12}>
              {recipe.description}
            </Col>
          </Row>
          <br/><br />
          <Row>
            <Col sm={6}>
              <h1>Ingredients</h1>
              <ListGroup variant="flush">
                {recipe.ingredients.map((ingredient, index) => (
                  <ListGroup.Item key={index}>{ingredient}</ListGroup.Item>
                ))}
              </ListGroup>
            </Col>
            <Col sm={6}>
              <h1>Preperation</h1>
              <ListGroup variant="flush">
                {recipe.preparaion_steps.map((step, index) => (
                  <ListGroup.Item key={index}>{step}</ListGroup.Item>
                ))}
              </ListGroup>
            </Col>
          </Row>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="secondary">Close</Button>
        </Modal.Footer>
      </Modal.Dialog>
    </div>
  )
}

function ShoppingList({ week, recipes }) {
  return (
    <Tabs defaultActiveKey="1">
      {Object.keys(week).map((key, index) => (key !== "week" && key !== "id" ?
          <Tab key={index} eventKey={key} title={"Day " + key}>
            {recipes.find(r => (r.id === week[key])).ingredients.map(ingredient =>
              <ListGroup.Item key={index}>{ingredient}</ListGroup.Item>
            )}
          </Tab>
          : "")
      )}
    </Tabs>
  )
}

function WeekOverview({ week }) {
  return (
    <Table striped bordered hover>
      <thead>
        <tr>
          <th>Day</th>
          <th>Recipe</th>
        </tr>
      </thead>
      <tbody>
        {Object.keys(week).map(key => (key !== "week" && key !== "id" ?
          <tr key={key}>
            <td>{key}</td>
            <td>{week[key]}</td>
          </tr> : null
        ))}
      </tbody>
    </Table>
  )
}